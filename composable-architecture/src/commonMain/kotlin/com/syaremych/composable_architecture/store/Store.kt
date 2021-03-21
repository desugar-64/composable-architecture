@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.store

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


typealias Reduced<Value, Action> = Pair<Value, Effect<Action>>

inline fun <Value, Action> reduced(
    value: Value,
    effect: Effect<Action>
): Reduced<Value, Action> = value to effect

inline fun <Value, Action> reduced(value: Value) =
    reduced<Value, Action>(value, Effect.none())

class Store<Value : Any, Action : Any> private constructor(
    private val storeDispatcher: CoroutineDispatcher
) {

    private lateinit var _valueHolder: MutableStateFlow<Value>

    val stateHolder: StateFlow<Value>
        get() = _valueHolder

    val state: Value
        get() = _valueHolder.value

    private lateinit var reducer: Reducer<Value, Action, Any>

    private lateinit var environment: Any

    internal val storeScope = CoroutineScope(SupervisorJob() + storeDispatcher)

    internal fun send(action: Action) {
        storeScope.launch {
//            println("Store send, thread = ${Thread.currentThread().name}:${Thread.currentThread().id}")
            val (value, effect) = reducer.reduce(_valueHolder.value, action, environment)
            this@Store._valueHolder.value = value
            ensureActive()
            effect
                .onEach(::send)
                .launchIn(this)
        }
    }

    fun <LocalValue : Any, LocalAction : Any> scope(
        toLocalValue: (Value) -> LocalValue,
        toGlobalAction: (LocalAction) -> Action
    ): Store<LocalValue, LocalAction> {
        val localStore = init<LocalValue, LocalAction, Any>(
            initialState = toLocalValue(_valueHolder.value),
            reducer = Reducer { _, localAction, _ ->
                this.send(toGlobalAction(localAction))
                val localValue = toLocalValue(_valueHolder.value)
                return@Reducer reduced(localValue, Effect.none())
            },
            environment = environment,
            storeDispatcher = storeDispatcher
        )

        _valueHolder
            .map { newValue -> toLocalValue(newValue) }
            .onEach { localStore._valueHolder.value = it }
            .launchIn(storeScope)

        return localStore
    }

    fun release() {
        storeScope.cancel()
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <Value : Any, Action : Any, Environment : Any> init(
            initialState: Value,
            reducer: Reducer<Value, Action, Environment>,
            environment: Environment,
            storeDispatcher: CoroutineDispatcher =
                StoreDispatchers.queueDispatcher() // Store must be single threaded
        ): Store<Value, Action> {
            val store = Store<Value, Action>(storeDispatcher)
            store._valueHolder = MutableStateFlow(initialState)
            store.environment = environment
            store.reducer = Reducer { value, action, env ->
                val (state, effect) = reducer.reduce(value, action, env as Environment)
                return@Reducer reduced(state, effect)
            }
            return store
        }
    }
}
