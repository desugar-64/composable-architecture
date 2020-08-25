@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.store

import android.util.Log
import com.syaremych.composable_architecture.store.Store.Subscriber
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.util.*
import kotlin.collections.HashSet


typealias Reduced<Value, Action> = Pair<Value, List<Effect<Action>>>

inline fun <Value, Action> reduced(
    value: Value,
    effects: List<Effect<Action>>
): Reduced<Value, Action> = value to effects

class Store<Value : Any, Action : Any> private constructor(
    storeDispatcher: CoroutineDispatcher
) {

    fun interface Subscriber<Value> {
        fun render(value: Value)
    }

    @Volatile
    lateinit var value: Value
        internal set

    private lateinit var reducer: Reducer<Value, Action, Any>

    private lateinit var environment: Any

    private val subscribers: MutableSet<Subscriber<Value>> =
        Collections.synchronizedSet(HashSet())

    private val storeScope = CoroutineScope(SupervisorJob() + storeDispatcher)

    internal var onStoreReleased: (() -> Unit)? = null

    fun subscribe(subscriber: Subscriber<Value>) {
        subscribers.add(subscriber)
        subscriber.render(value)
    }

    internal fun send(action: Action) {
        storeScope.launch {
            val (value, effects) = reducer(value, action, environment)
            this@Store.value = value
            effects.forEach { effect ->
                ensureActive()
                effect.collect { action: Action -> send(action) }
            }
            notifySubscribers()
        }
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

        val storeToViewUpdateNotifier = Subscriber<Value> { value ->
            localStore.value = toLocalValue(value)
            localStore.notifySubscribers()
            Log.d("Local Store", "subscribe#render invoked")
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
        storeScope.cancel()
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
            environment: Environment,
            storeDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate
        ): Store<Value, Action> {
            val store = Store<Value, Action>(storeDispatcher)
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
