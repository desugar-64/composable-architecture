package com.syaremych.composable_architecture.store

import android.util.Log
import java.util.*

class ViewStore<Value : Any, Action: Any>(
    initialValue: Value,
    val send: (Action) -> Unit,
    private val acceptUpdateIf: (Value, Value) -> Boolean
) {
    var value: Value = initialValue
        internal set

    private val subscribers: MutableSet<Store.Subscriber<Value>> =
        Collections.synchronizedSet(HashSet())

    internal val subscriber = object : Store.Subscriber<Value> {
        override fun render(value: Value) {
            if (acceptUpdateIf(this@ViewStore.value, value)) {
                this@ViewStore.value = value
                notifySubscribers()
            }
        }
    }

    fun subscribe(subscriber: Store.Subscriber<Value>) {
        subscribers.add(subscriber)
        subscriber.render(value)
    }

    fun unsubscribe(subscriber: Store.Subscriber<Value>) {
        subscribers.remove(subscriber)
    }

    private fun notifySubscribers() {
        Log.d("Store", "notifySubscribers invoked. Subscribers count: ${subscribers.size}")
        subscribers.forEach { it.render(value) }
    }

}

fun <Value, Action> Store<Value, Action>.view(
    acceptUpdateIf: (Value, Value) -> Boolean
): ViewStore<Value, Action> where Value: Any, Action: Any {
    val viewStore = ViewStore(
        initialValue = value,
        send = ::send,
        acceptUpdateIf = acceptUpdateIf
    )
    onStoreReleased = { unsubscribe(viewStore.subscriber) }
    return viewStore
}

val <Value : Any, Action : Any> Store<Value, Action>.view: ViewStore<Value, Action>
    get() = view(acceptUpdateIf = { lhs, rhs -> lhs == rhs})
