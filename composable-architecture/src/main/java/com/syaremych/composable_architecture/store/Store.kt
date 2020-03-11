@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.store

import android.util.Log
import com.syaremych.composable_architecture.prelude.pipe
import com.syaremych.composable_architecture.prelude.withA
import java.util.*
import java.util.concurrent.Executor

typealias Callback<A> = (A) -> Unit

class Effect<out A>(val run: (Callback<A>) -> Unit)

fun <A, B> Effect<A>.fmap(f: (A) -> Effect<B>): Effect<B> {
//    return Effect { cb -> this@fmap.run { f(it).run(cb) } }
    return Effect { callback ->
        run { a -> withA(a, f).run(callback) }
    }
}

fun <A, B> map(f: (A) -> B): (Effect<A>) -> Effect<B> {
    return { effectA ->
        Effect { callback ->
//          effectA.run // (A -> Unit) -> Unit
//          callback // B -> Unit
//          pipe(f, callback) // A -> Unit
            effectA.run(pipe(f, callback))
        }
    }
}

fun <A, B> Effect<A>.map(f: (A) -> B): Effect<B> =
    Effect { callback ->
//      run // (A -> Unit) -> Unit
//      callback // B -> Unit
//      pipe(f, callback) // A -> Unit
        run(pipe(f, callback))
    }

fun <A> Effect<A>.receiveOn(executor: Executor): Effect<A> {
    return Effect { callback ->
        this.run { action -> executor.execute { callback(action) } }
    }
}

typealias Reduced<Value, Action> = Pair<Value, List<Effect<Action>>>
typealias Reducer<Value, Action> = (value: Value, action: Action) -> Reduced<Value, Action>

inline fun <Action> emptyEffect(): Effect<Action> = Effect { }

inline fun <Action> noEffects(): List<Effect<Action>> = emptyList()
inline fun <Value, Action> reduced(
    value: Value,
    effects: List<Effect<Action>>
): Reduced<Value, Action> = value to effects

class Store<Value, Action>(
    private val initialState: Value,
    private val reducer: Reducer<Value, Action>
) {
    interface Subscriber<Value> {
        fun render(value: Value)
    }

    var value: Value = initialState
        private set

    private val subscribers: MutableSet<Subscriber<Value>> =
        Collections.synchronizedSet(HashSet())

    private var onStoreReleased: (() -> Unit)? = null

    fun subscribe(subscriber: Subscriber<Value>) {
        subscribers.add(subscriber)
        subscriber.render(value)
    }

    fun send(action: Action) {
        val (value, effects) = reducer(value, action)
        this.value = value
        effects.forEach { effect -> effect.run(::send) }
        notifySubscribers()
    }

    fun <LocalValue, LocalAction> view(
        toLocalValue: (Value) -> LocalValue,
        toGlobalAction: (LocalAction) -> Action
    ): Store<LocalValue, LocalAction> {
        val localStore = Store(
            initialState = toLocalValue(value),
            reducer = { _, localAction: LocalAction ->
                this.send(toGlobalAction(localAction))
                val localValue = toLocalValue(value)
                return@Store reduced(localValue, noEffects())
            }
        )

        val storeToViewUpdateNotifier = object : Subscriber<Value> {
            override fun render(value: Value) {
                localStore.value = toLocalValue(value)
                localStore.notifySubscribers()
                Log.d("Local Store", "subscribe#render invoked")
            }
        }

        localStore.onStoreReleased = {
            unsubscribe(storeToViewUpdateNotifier)
        }

        subscribe(storeToViewUpdateNotifier) // FIXME: leaking when Store#view function called multiple times, storeToViewUpdateNotifier never get removed

        return localStore
    }

    fun release() {
        subscribers.clear()
        onStoreReleased?.invoke()
        onStoreReleased = null
    }

    fun unsubscribe(subscriber: Subscriber<Value>) {
        subscribers.remove(subscriber)
    }

    private fun notifySubscribers() {
        Log.d("Store", "notifySubscribers invoked. Subscribers count: ${subscribers.size}")
        subscribers.forEach { it.render(value) }
    }
}
