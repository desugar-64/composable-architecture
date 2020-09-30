package com.sergeyfitis.moviekeeper.statemanagement.store

import org.junit.Assert
import org.junit.Test


class StoreTest {

    @Test
    fun view() {
        val store =
            com.syaremych.composable_architecture.store.Store<Int, Unit>(0) { value, _ ->
                com.syaremych.composable_architecture.store.reduced(
                    value.inc(),
                    com.syaremych.composable_architecture.store.noEffects()
                )
            }
        store.send(Unit)
        store.send(Unit)
        Assert.assertEquals(store.valueHolder, 2)

        val newStore = store.view<Int, Unit>(
            toLocalValue = { it },
            toGlobalAction = { it }
        )
        Assert.assertEquals(newStore.value, 2)
        newStore.send(Unit)
        newStore.send(Unit)
        newStore.send(Unit)
        Assert.assertEquals(newStore.value, 5)
        Assert.assertEquals(store.valueHolder, 5)
        store.send(Unit)
        Assert.assertEquals(newStore.value, 6)
        Assert.assertEquals(store.valueHolder, 6)
    }
}