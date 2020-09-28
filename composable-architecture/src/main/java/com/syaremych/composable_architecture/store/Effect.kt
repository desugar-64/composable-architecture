@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.store

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class Effect<out A>(flow: Flow<A>) : Flow<A> {
    private val upstream: Flow<A> = flow

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<A>) {
        upstream.collect(collector)
    }

    fun runOn(dispatcher: CoroutineDispatcher): Effect<A> {
        return upstream.flowOn(dispatcher).eraseToEffect()
    }

    companion object
}

fun <T> Flow<T>.eraseToEffect(): Effect<T> = Effect(this)

inline fun <Action> emptyEffect(): Effect<Action> = emptyFlow<Action>().eraseToEffect()

inline fun <Action> noEffects(): List<Effect<Action>> = emptyList()

fun <T> Effect.Companion.sync(work: () -> T): Effect<T> =
    flowOf(work()).eraseToEffect()

fun <T> Effect.Companion.fireAndForget(work: () -> Unit): Effect<T> =
    flow<T> { work(); emitAll(emptyFlow()) }.eraseToEffect()

// TODO: replace later with Flow#flattenMerge api
fun <T> Effect.Companion.merge(vararg effects: Effect<T>): Effect<T> = channelFlow<T> {
    val ch = this.channel
    effects.forEach { effect ->
        launch {
            effect.collect(ch::send)
        }
    }
}.eraseToEffect()
