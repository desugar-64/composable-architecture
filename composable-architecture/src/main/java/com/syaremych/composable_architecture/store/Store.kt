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

inline fun <Action> emptyEffect(): Effect<Action> = Effect { }

inline fun <Action> noEffects(): List<Effect<Action>> = emptyList()
inline fun <Value, Action> reduced(
    value: Value,
    effects: List<Effect<Action>>
): Reduced<Value, Action> = value to effects

class Store<Value : Any, Action : Any> private constructor() {

    interface Subscriber<Value> {
        fun render(value: Value)
    }

    @Volatile
    lateinit var value: Value
        internal set

    private lateinit var reducer: Reducer<Value, Action, Any>

    private lateinit var environment: Any

    private val subscribers: MutableSet<Subscriber<Value>> =
        Collections.synchronizedSet(HashSet())

    internal var onStoreReleased: (() -> Unit)? = null

    fun subscribe(subscriber: Subscriber<Value>) {
        subscribers.add(subscriber)
        subscriber.render(value)
    }

    internal fun send(action: Action) {
        val (value, effects) = reducer(value, action, environment)
        this.value = value
        effects.forEach { effect -> effect.run(::send) }
        notifySubscribers()
    }

    fun <LocalValue : Any, LocalAction : Any> scope(
        toLocalValue: (Value) -> LocalValue,
        toGlobalAction: (LocalAction) -> Action
    ): Store<LocalValue, LocalAction> {
        val localStore = init<LocalValue, LocalAction, Any>(
            initialState = toLocalValue(value),
            reducer = Reducer { _, localAction, _ ->
                this.send(toGlobalAction(localAction))
                val localValue = toLocalValue(value)
                return@Reducer reduced(localValue, noEffects())
            },
            environment = environment
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

        subscribe(storeToViewUpdateNotifier)

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

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <Value : Any, Action : Any, Environment : Any> init(
            initialState: Value,
            reducer: Reducer<Value, Action, Environment>,
            environment: Environment
        ): Store<Value, Action> {
            val store = Store<Value, Action>()
            store.value = initialState
            store.environment = environment
            store.reducer = Reducer { value, action, env ->
                val (state, effects) = reducer(value, action, env as Environment)
                return@Reducer reduced(state, effects)
            }
            return store
        }
    }
}
