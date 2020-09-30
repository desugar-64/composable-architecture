@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.store

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class Effect<A>(flow: Flow<A>) : AbstractFlow<A>() {
    private val upstream: Flow<A> = flow

    var isEmpty: Boolean = false

    @OptIn(InternalCoroutinesApi::class)
    override suspend fun collectSafely(collector: FlowCollector<A>) {
        upstream.collect(collector)
    }

    fun runOn(dispatcher: CoroutineDispatcher): Effect<A> {
        return upstream.flowOn(dispatcher).eraseToEffect()
    }

    companion object
}

fun <T> Flow<T>.eraseToEffect(): Effect<T> = Effect(this)

@Deprecated(
    "Use Effect.none()", ReplaceWith(
        "Effect.none()",
        "com.syaremych.composable_architecture.store.none"
    )
)
inline fun <Action> emptyEffect(): Effect<Action> = listOf<Action>().asFlow().eraseToEffect()

@Deprecated(
    "Obsolete", ReplaceWith(
        "Effect.none()",
        "com.syaremych.composable_architecture.store.none"
    )
)
inline fun <Action> noEffects(): List<Effect<Action>> = emptyList()

inline fun <Action> Effect.Companion.none() =
    emptyFlow<Action>().eraseToEffect().apply { isEmpty = true }

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
