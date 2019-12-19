package com.sergeyfitis.moviekeeper.statemanagement.store

typealias Reducer<Value, Action> =(value: Value, action: Action) -> Unit

class Store<Value, Action>(
    initialState: Value,
    reducer: Reducer<Value, Action>
) {
    interface Subscriber<Value> { fun render(value: Value) }

    val value: Value = initialState

    private val reducer: Reducer<Value, Action> = reducer

    private val subscribers: MutableSet<Subscriber<Value>> = HashSet()

    fun subscribe(subscriber: Subscriber<Value>) {
        subscribers.add(subscriber)
        subscriber.render(value)
    }

    fun unsubscribe(subscriber: Subscriber<Value>) {
        subscribers.remove(subscriber)
    }

    fun send(action: Action) {
        reducer(value, action)
        subscribers.forEach { it.render(value) }
    }
}