package com.sergeyfitis.moviekeeper.statemanagement.store

import org.junit.Assert
import org.junit.Test


class StoreTest {

    @Test
    fun view() {
        val store =
            Store<Int, Unit>(0) { value, _ -> listOf(value.inc() to noEffect()) }
        store.send(Unit)
        store.send(Unit)
        Assert.assertEquals(store.value, 2)

        val newStore = store.view<Int, Unit>(
            toLocalValue = { it },
            toGlobalAction = { it }
        )
        Assert.assertEquals(newStore.value, 2)
        newStore.send(Unit)
        newStore.send(Unit)
        newStore.send(Unit)
        Assert.assertEquals(newStore.value, 5)
        Assert.assertEquals(store.value, 5)
        store.send(Unit)
        Assert.assertEquals(newStore.value, 6)
        Assert.assertEquals(store.value, 6)
    }
}