package com.sergeyfitis.moviekeeper.prelude

fun <Value, Action> combine(
    vararg reducers: (Value, Action) -> Unit
): (Value, Action) -> Unit {
    return { value, action ->
        for (reducer in reducers) {
            reducer(value, action)
        }
    }
}

fun <LocalValue, GlobalValue, LocalAction, GlobalAction> pullback(
    reducer: (LocalValue, LocalAction) -> Unit,
    valueGet: (GlobalValue) -> LocalValue,
    valueSet: (GlobalValue, LocalValue) -> Unit,
    actionGet: (GlobalAction) -> LocalAction?
): (GlobalValue, GlobalAction) -> Unit {
    return { globalValue, globalAction ->
        val localAction = actionGet(globalAction)
        if (localAction != null) {
            val localValue = valueGet(globalValue)
            reducer(localValue, localAction)
            valueSet(globalValue, localValue)
        }
    }
}