package com.syaremych.composable_architecture.compose_ui

import androidx.compose.runtime.*
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.store.Store

@Composable
inline fun <State, Action : Any> IfLetStore(
    store: Store<State?, Action>,
    elseContent: @Composable () -> Unit = {},
    ifContent: @Composable (Store<State, Action>) -> Unit
) {
    val state = store.state
    if (state != null) {
        val nonNullStateStore: Store<State, Action> = remember(store) {
            store.scope(toLocalValue = { it ?: state }, toGlobalAction = ::identity)
        }
        DisposableEffect(nonNullStateStore) {
            onDispose(nonNullStateStore::release)
        }
        ifContent(nonNullStateStore)
    } else {
        elseContent()
    }
}