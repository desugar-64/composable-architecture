@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.store

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.experimental.ExperimentalTypeInference


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTypeInference::class)
class Effect<out A>(private val upstream: Flow<A>) : Flow<A> {

    constructor(@BuilderInference block: suspend FlowCollector<A>.() -> Unit) : this(flow(block))

    constructor(value: A) : this(flowOf(value))

    @OptIn(InternalCoroutinesApi::class)
    override suspend fun collect(collector: FlowCollector<A>) {
        upstream.collect(collector)
    }

    fun runOn(dispatcher: CoroutineDispatcher): Effect<A> {
        return flowOn(dispatcher).eraseToEffect()
    }

    companion object
}

fun <T> Flow<T>.eraseToEffect(): Effect<T> = Effect(this)

inline fun <Action> Effect.Companion.none() =
    Effect(emptyFlow<Action>())

fun <T> Effect.Companion.sync(work: () -> T): Effect<T> =
    Effect(work())

fun <T> Effect.Companion.fireAndForget(work: () -> Unit): Effect<T> =
    Effect { work(); emitAll(emptyFlow()) }

/**
 * Merges a sequence of effects together into a single effect, which runs the effects at the same
 * time.
 */
fun <T> Effect.Companion.merge(vararg effects: Effect<T>): Effect<T> {
    if (effects.isEmpty()) {
        return none()
    }
    return effects
        .asFlow()
        .flattenMerge(concurrency = effects.size)
        .eraseToEffect()
}

fun <T> Effect.Companion.merge(vararg flows: Flow<T>): Effect<T> {
    if (flows.isEmpty()) {
        return none()
    }
    return flows
        .asFlow()
        .flattenMerge(concurrency = flows.size).eraseToEffect()
}

fun <T> Effect.Companion.merge(effects: List<Effect<T>>): Effect<T> {
    if (effects.isEmpty()) {
        return none()
    }
    return effects
        .asFlow()
        .flattenMerge(concurrency = effects.size)
        .eraseToEffect()
}

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