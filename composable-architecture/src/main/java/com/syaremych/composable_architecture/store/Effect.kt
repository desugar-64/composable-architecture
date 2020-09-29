@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.store

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

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

/**
 * Merges a sequence of effects together into a single effect, which runs the effects at the same
 * time.
 */
fun <T> Effect.Companion.merge(vararg effects: Effect<T>): Effect<T> =
    effects
        .asFlow()
        .flattenMerge(concurrency = effects.size)
        .eraseToEffect()

/**
 * Concatenates a collection of effects together into a single effect,
 * which runs the effects one after the other.
 */
fun <T> Effect.Companion.concat(vararg effects: Effect<T>): Effect<T> =
    effects
        .asFlow()
        .flattenConcat()
        .eraseToEffect()
