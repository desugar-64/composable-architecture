package com.syaremych.composable_architecture.store

import kotlinx.coroutines.CoroutineDispatcher

expect object StoreDispatchers {
    fun queueDispatcher(): CoroutineDispatcher
}