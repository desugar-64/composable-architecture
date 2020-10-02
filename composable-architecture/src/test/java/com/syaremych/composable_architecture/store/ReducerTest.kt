package com.syaremych.composable_architecture.store

import com.nhaarman.mockitokotlin2.*
import com.syaremych.composable_architecture.store.StoreTest.Act
import com.syaremych.composable_architecture.store.StoreTest.IntState
import com.syaremych.composable_architecture.util.TestReducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReducerTest {
    private enum class ReducerOrder { FST, SND, TRD }

    @Test
    fun reducer_CallReduce() = runBlocking {
        val state = IntState(0, 0, 0)
        val testReducer = TestReducer<IntState, Act, Unit>()
        val reducer = testReducer.prepareReducer { intState, _, _ ->
            reduced(intState.copy(fst = intState.fst + 1))
        }

        val store = Store.init(
            initialState = state,
            reducer = reducer,
            environment = Unit
        )

        assertEquals(state, store.stateHolder.value)
        assertEquals(0, testReducer.callCount)

        store.send(Act.IncFst)

        testReducer.awaitReduce()

        assertEquals(1, testReducer.callCount)
        assertEquals(state.copy(fst = 1), store.stateHolder.value)
    }

    @Test
    fun reducer_Combine() = runBlocking {
        val state = IntState(0, 0, 0)
        val testReducerFst = TestReducer<IntState, Act, Unit>()
        val testReducerSnd = TestReducer<IntState, Act, Unit>()
        val testReducerTrd = TestReducer<IntState, Act, Unit>()

        val fstReducer = testReducerFst.prepareReducer { intState, act, _ ->
            reduced(intState.copy(fst = intState.fst + 1))
        }

        val sndReducer = testReducerSnd.prepareReducer { intState, act, _ ->
            reduced(intState.copy(snd = intState.snd + 1))
        }

        val trdReducer = testReducerTrd.prepareReducer { intState, act, _ ->
            reduced(intState.copy(trd = intState.trd + 1))
        }

        val combinedReducer = Reducer.combine(fstReducer, sndReducer, trdReducer)

        val store = Store.init(
            initialState = state,
            reducer = combinedReducer,
            environment = Unit
        )

        assertEquals(state, store.stateHolder.value)
        assertEquals(0, testReducerFst.callCount)
        assertEquals(0, testReducerSnd.callCount)
        assertEquals(0, testReducerTrd.callCount)

        store.send(Act.Inc)
        store.send(Act.Inc)
        store.send(Act.Inc)

        repeat(3) {
            testReducerFst.awaitReduce()
            testReducerSnd.awaitReduce()
            testReducerTrd.awaitReduce()
        }

        assertEquals(3, testReducerFst.callCount)
        assertEquals(3, testReducerSnd.callCount)
        assertEquals(3, testReducerTrd.callCount)

        assertEquals(state.copy(fst = 3, snd = 3, trd = 3), store.stateHolder.value)
    }

    @Test
    fun reducer_EmptyReducerShouldNotUpdateState() {
        val state = IntState(0, 0, 0)
        val emptyReducer = Reducer.empty<IntState, Act, Unit>()
        val store = Store.init(
            initialState = state,
            reducer = emptyReducer,
            environment = Unit,
            storeDispatcher = Dispatchers.Unconfined
        )
        assertEquals(state, store.stateHolder.value)

        store.send(Act.Inc)
        assertEquals(state, store.stateHolder.value)
    }

    @Test
    fun reducer_EffectPassThroughTheChain() = runBlocking {
        val state = IntState(0, 0, 0)
        val effect = mock<Effect<Act.Inc>>()

        val expectedState = state.copy(fst = 1)

        val mockReducer = mock<Reducer<IntState, Act.Inc, Unit>> {
            on { reduce(any(), any(), any()) } doReturn reduced(expectedState, effect)
        }

        val store = Store.init(
            initialState = state,
            reducer = Reducer.combine(mockReducer),
            environment = Unit,
            storeDispatcher = Dispatchers.Unconfined
        )

        assertEquals(state, store.stateHolder.value)

        store.send(Act.Inc)
        val stateCaptor = argumentCaptor<IntState>()
        val envCaptor = argumentCaptor<Unit>()
        val actCaptor = argumentCaptor<Act.Inc>()

        verify(mockReducer).reduce(stateCaptor.capture(), actCaptor.capture(), envCaptor.capture())
        val reducedAction = actCaptor.firstValue

        assertEquals(expectedState, store.stateHolder.value)
        assertEquals(Act.Inc, reducedAction)

        verify(effect).collect(any())
    }

    @Test
    fun reducer_CombineCallsAllCombinedReducersInOrder() {
        val state = IntState(0, 0, 0)
        val reducerCallOrder = mutableListOf<ReducerOrder>()
        val reducerFst = Reducer<IntState, Act.Inc, Unit> { intState, _, _ ->
            reducerCallOrder.add(ReducerOrder.FST)
            reduced(intState)
        }
        val reducerSnd = Reducer<IntState, Act.Inc, Unit> { intState, _, _ ->
            reducerCallOrder.add(ReducerOrder.SND)
            reduced(intState)
        }
        val reducerTrd = Reducer<IntState, Act.Inc, Unit> { intState, _, _ ->
            reducerCallOrder.add(ReducerOrder.TRD)
            reduced(intState)
        }

        val expectedCallOrder = ReducerOrder.values().toList()

        val combinedReducer =
            Reducer.combine(reducerFst, reducerSnd, reducerTrd)

        combinedReducer.reduce(state, Act.Inc, Unit)

        assertEquals(expectedCallOrder, reducerCallOrder)
    }
}