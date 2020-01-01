@file:Suppress("NOTHING_TO_INLINE")

package com.sergeyfitis.moviekeeper.statemanagement.store

typealias Effect<Action> = (callback: (Action) -> Unit) -> Unit
typealias Reduced<Value, Action> = Pair<Value, List<Effect<Action>>>
typealias Reducer<Value, Action> = (value: Value, action: Action) -> Reduced<Value, Action>

inline fun <Action> noEffect(): Effect<Action> = {  }
inline fun <Action> noEffects(): List<Effect<Action>> = emptyList()
inline fun <Action> effect(noinline effect: Effect<Action>): Effect<Action> = effect
inline fun <Value, Action> reduced(value: Value, effects: List<Effect<Action>>): Reduced<Value, Action> =
    value to effects

class Store<Value, Action>(
    private val initialState: Value,
    private val reducer: Reducer<Value, Action>
) {
    interface Subscriber<Value> {
        fun render(value: Value)
    }

    var value: Value = initialState
        private set

    private val subscribers: MutableSet<Subscriber<Value>> = HashSet()

    fun subscribe(subscriber: Subscriber<Value>) {
        subscribers.add(subscriber)
        subscriber.render(value)
    }

    fun unsubscribe(subscriber: Subscriber<Value>) {
        subscribers.remove(subscriber)
    }

    fun send(action: Action) {
        val (value, effects) = reducer(value, action)
        this.value = value
        for (effect in effects) {
            effect(::send)
        }
        subscribers.forEach { it.render(value) }
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

        subscribe(object : Subscriber<Value> {
            override fun render(value: Value) {
                localStore.value = toLocalValue(value)
            }
        })
        return localStore
    }
}
