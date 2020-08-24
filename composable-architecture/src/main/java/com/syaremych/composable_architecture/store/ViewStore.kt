package com.syaremych.composable_architecture.store

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.syaremych.composable_architecture.prelude.concat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.*

class ViewStore<Value : Any, Action: Any>(
    initialValue: Value,
    val send: (Action) -> Unit,
    private val acceptUpdateIf: (Value, Value) -> Boolean,
    val doOnDispose: () -> Unit
) : LifecycleObserver {
    var value: Value = initialValue
        @Synchronized internal set
        @Synchronized get

    private val subscribers: MutableSet<Store.Subscriber<Value>> =
        Collections.synchronizedSet(HashSet())

    private val mainThread = Dispatchers.Main.immediate.asExecutor()


    internal val subscriber =
        Store.Subscriber<Value> { value ->
            if (acceptUpdateIf(this@ViewStore.value, value)) {
                this@ViewStore.value = value
                notifySubscribers()
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
        Log.d("ViewStore", "notifySubscribers invoked. Subscribers count: ${subscribers.size}")
        mainThread.execute { subscribers.forEach { it.render(value) } }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy() {
        doOnDispose.invoke()
        Log.d("ViewStore", "destroyed")
        subscribers.clear()
    }

}

fun <Value, Action> Store<Value, Action>.view(
    acceptUpdateIf: (Value, Value) -> Boolean
): ViewStore<Value, Action> where Value: Any, Action: Any {
    val viewStore = ViewStore(
        initialValue = value,
        send = ::send,
        acceptUpdateIf = acceptUpdateIf,
        doOnDispose = ::release
    )
    val doOnRelease = onStoreReleased concat { unsubscribe(viewStore.subscriber) }
    onStoreReleased = doOnRelease
    subscribe(viewStore.subscriber)
    return viewStore
}

val <Value : Any, Action : Any> Store<Value, Action>.view: ViewStore<Value, Action>
    get() = view(acceptUpdateIf = { lhs, rhs -> lhs != rhs })
