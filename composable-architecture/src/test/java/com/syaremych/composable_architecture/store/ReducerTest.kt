package com.syaremych.composable_architecture.store

import com.syaremych.composable_architecture.store.StoreTest.Act
import com.syaremych.composable_architecture.store.StoreTest.IntState
import com.syaremych.composable_architecture.util.TestReducer
import kotlinx.coroutines.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executors

@RunWith(MockitoJUnitRunner::class)
class ReducerTest {

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
    fun reducer_Combine()  = runBlocking {
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

        assertEquals(1, testReducerFst.callCount)
        assertEquals(1, testReducerSnd.callCount)
        assertEquals(1, testReducerTrd.callCount)

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
}