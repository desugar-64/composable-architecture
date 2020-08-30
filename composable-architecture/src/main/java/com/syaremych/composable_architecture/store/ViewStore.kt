package com.syaremych.composable_architecture.store

import com.syaremych.composable_architecture.prelude.concat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalCoroutinesApi::class)
class ViewStore<Value : Any, Action : Any>(
    initialValue: Value,
    val send: (Action) -> Unit,
    private val acceptUpdateIf: (Value, Value) -> Boolean,
    private val doOnDispose: () -> Unit
) : Flow<Value> {

    private val _viewState: MutableStateFlow<Value> = MutableStateFlow(initialValue)

    val value: Value
        get() = _viewState.value

    internal val subscriber =
        Store.Subscriber<Value> { value ->
            if (acceptUpdateIf(this@ViewStore._viewState.value, value)) {
                this@ViewStore._viewState.value = value
            }
        }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Value>) {
        _viewState.collect(collector)
    }

    fun dispose() = doOnDispose.invoke()
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
