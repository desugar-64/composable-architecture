@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.store

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.experimental.ExperimentalTypeInference

interface Effect<A> : Flow<A>{
    fun runOn(dispatcher: CoroutineDispatcher): Effect<A>
    companion object
}

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTypeInference::class)
internal class EffectImpl<A>(@BuilderInference private val block: suspend FlowCollector<A>.() -> Unit) : AbstractFlow<A>(), Effect<A> {

    @OptIn(InternalCoroutinesApi::class)
    override suspend fun collectSafely(collector: FlowCollector<A>) {
        collector.block()
    }

    override fun runOn(dispatcher: CoroutineDispatcher): Effect<A> {
        return flowOn(dispatcher).eraseToEffect()
    }

    companion object
}

fun <T> Flow<T>.eraseToEffect(): Effect<T> = EffectImpl { emitAll(this@eraseToEffect) }


inline fun <Action> Effect.Companion.none() =
    emptyFlow<Action>().eraseToEffect()

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

fun <T> Effect.Companion.merge(vararg flows: Flow<T>): Effect<T> =
    flows
        .asFlow()
        .flattenMerge(concurrency = flows.size).eraseToEffect()

fun <T> Effect.Companion.merge(effects: List<Effect<T>>): Effect<T> =
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

fun <T> Effect.Companion.concat(flows: List<Flow<T>>): Effect<T> =
    flows
        .asFlow()
        .flattenConcat()
        .eraseToEffect()