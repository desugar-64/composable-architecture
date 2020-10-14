package com.syaremych.composable_architecture.store

import com.syaremych.composable_architecture.store.StoreTest.Act
import com.syaremych.composable_architecture.store.StoreTest.IntState
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ViewStoreTest {

    @Before
    fun setUp() {
    }

    @Test
    fun viewStore_StateUpdate() {
        val state = IntState(0, 0, 0)
        val reducer = Reducer<IntState, Act, Unit> { intState, act, _ ->
            when(act) {
                Act.Inc -> reduced(intState) // ignore any updates
                Act.IncFst -> reduced(intState.copy(fst = intState.fst + 1))
                Act.IncSnd -> reduced(intState.copy(snd = intState.snd + 1))
                Act.IncTrd -> reduced(intState.copy(trd = intState.trd + 1))
            }
        }
        val store = Store.init(
            initialState = state,
            reducer = reducer,
            environment = Unit,
            storeDispatcher = Dispatchers.Unconfined
        )

        assertEquals(state, store.state)

        val viewStore = store.view

        assertEquals(state, viewStore.state)

        viewStore.send(Act.Inc)
        assertEquals(state, viewStore.state) // nothing should change

        store.send(Act.IncFst)
        assertEquals(state.copy(fst = 1, snd = 0, trd = 0), store.state)
        assertEquals(state.copy(fst = 1, snd = 0, trd = 0), viewStore.state)

        viewStore.send(Act.IncFst)
        assertEquals(state.copy(fst = 2, snd = 0, trd = 0), viewStore.state)
        assertEquals(state.copy(fst = 2, snd = 0, trd = 0), store.state) // update must be propagated to the parent state
    }

    @Test
    fun viewStore_DisposedShouldNotReceiveUpdates() {
        val state = IntState(0, 0, 0)
        val reducer = Reducer<IntState, Act, Unit> { intState, act, _ ->
            when(act) {
                Act.Inc -> reduced(intState) // ignore any updates
                Act.IncFst -> reduced(intState.copy(fst = intState.fst + 1))
                Act.IncSnd -> reduced(intState.copy(snd = intState.snd + 1))
                Act.IncTrd -> reduced(intState.copy(trd = intState.trd + 1))
            }
        }
        val store = Store.init(
            initialState = state,
            reducer = reducer,
            environment = Unit,
            storeDispatcher = Dispatchers.Unconfined
        )

        assertEquals(state, store.state)

        val viewStore = store.view

        assertEquals(state, viewStore.state)

        viewStore.dispose()

        viewStore.send(Act.IncFst)
        assertEquals(state, viewStore.state)
        assertEquals(state, store.state)
    }
}