package com.syaremych.composable_architecture.compose_ui

import androidx.compose.runtime.*
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view

@Composable
fun <State : Any, Action : Any, ViewState : Any, ViewAction : Any> rememberViewStore(
    store: Store<State, Action>,
    toViewState: (State) -> ViewState,
    toAction: (ViewAction) -> Action
): ViewStore<ViewState, ViewAction> {
    val toLocalValue by rememberUpdatedState(toViewState)
    val toGlobalAction by rememberUpdatedState(toAction)
    val viewStore = remember(store) {
        store.scope(toLocalValue = toLocalValue, toGlobalAction = toGlobalAction).view
    }
    DisposableEffect(viewStore) {
        onDispose(viewStore::dispose)
    }
    return viewStore
}

@Composable
fun <State : Any, Action : Any> rememberViewStore(store: Store<State, Action>): ViewStore<State, Action> {
    val viewStore = remember(store, store::view)
    DisposableEffect(viewStore) {
        onDispose(viewStore::dispose)
    }
    return viewStore
}

@Composable
fun <S : Any, A : Any> ViewStore<S, A>.collectAsState(): State<S> = collectAsState(state)