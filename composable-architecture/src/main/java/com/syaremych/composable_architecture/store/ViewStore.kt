package com.syaremych.composable_architecture.store

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class ViewStore<Value : Any, Action : Any>(
    initialValue: Value,
    val send: (Action) -> Unit,
    private val doOnDispose: () -> Unit
) : Flow<Value> {

    internal val _viewState: MutableStateFlow<Value> = MutableStateFlow(initialValue)

    val value: Value
        get() = _viewState.value

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
        initialValue = valueHolder.value,
        send = ::send,
        doOnDispose = ::release
    )

    valueHolder
        .distinctUntilChanged(acceptUpdateIf)
        .onEach { viewStore._viewState.value = it }
        .launchIn(storeScope)

    return viewStore
}

val <Value : Any, Action : Any> Store<Value, Action>.view: ViewStore<Value, Action>
    get() = view(acceptUpdateIf = { lhs, rhs -> lhs != rhs })
