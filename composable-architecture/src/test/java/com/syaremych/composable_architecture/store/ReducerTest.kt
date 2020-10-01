package com.syaremych.composable_architecture.store

import com.syaremych.composable_architecture.store.StoreTest.Act
import com.syaremych.composable_architecture.store.StoreTest.IntState
import com.syaremych.composable_architecture.util.TestReducer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReducerTest {

    @Test
    fun reducer_CallReduce() = runBlocking {
        val state = IntState(0, 0, 0)
        val testReducer = TestReducer<IntState, Act, Unit>()
        val reducer = testReducer.prepareReducer { intState, act, unit ->
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
}