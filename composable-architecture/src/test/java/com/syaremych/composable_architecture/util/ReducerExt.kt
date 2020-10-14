package com.syaremych.composable_architecture.util

import com.syaremych.composable_architecture.store.Reduced
import com.syaremych.composable_architecture.store.Reducer
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import java.util.concurrent.atomic.AtomicInteger

internal class TestReducer<V : Any, A : Any, E : Any> {

    private val reduceChannel = ConflatedBroadcastChannel<Unit>()
    private val _callCount = AtomicInteger(0)

    val callCount: Int get() = _callCount.get()

    fun prepareReducer(reduce: (V, A, E) -> Reduced<V, A>): Reducer<V, A, E> {
        val testReduce = { v: V, a: A, e: E ->
            reduce(v, a, e).also {
                _callCount.incrementAndGet()
                reduceChannel.offer(Unit)
            }
        }
        return Reducer(testReduce)
    }

    suspend fun awaitReduce() =
        reduceChannel.openSubscription().receive()
}