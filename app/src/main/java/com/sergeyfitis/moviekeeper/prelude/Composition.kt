package com.sergeyfitis.moviekeeper.prelude

import com.sergeyfitis.moviekeeper.statemanagement.store.Reducer
import com.sergeyfitis.moviekeeper.statemanagement.store.effectOf
import com.sergeyfitis.moviekeeper.statemanagement.store.noEffect
import com.sergeyfitis.moviekeeper.statemanagement.store.reduced

fun <Value, Action> combine(
    vararg reducers: Reducer<Value, Action>
): Reducer<Value, Action> {
    return { value, action -> reducers.flatMap { it(value, action) } }
}

fun <LocalValue, GlobalValue, LocalAction, GlobalAction> pullback(
    reducer: Reducer<LocalValue, LocalAction>,
    valueGet: (GlobalValue) -> LocalValue,
    valueSet: (GlobalValue, LocalValue) -> Unit,
    toLocalAction: (GlobalAction) -> LocalAction?,
    toGlobalAction: (LocalAction?) -> GlobalAction?
    ): Reducer<GlobalValue, GlobalAction> {
    return globalReducer@{ globalValue, globalAction ->
        val localAction = toLocalAction(globalAction)
            ?: return@globalReducer reduced(globalValue, noEffect())
        val localValue = valueGet(globalValue)
        val localEffects = reducer(localValue, localAction)
        return@globalReducer localEffects.flatMap { (value, effect) ->
            valueSet(globalValue, value)
            return@flatMap reduced(
                value = globalValue,
                effect = effectOf(toGlobalAction(effect()))
            )
        }
    }
}
/*

fun <LocalValue, GlobalValue, LocalAction, GlobalAction> pullback(
    reducer: Reducer<LocalValue, LocalAction>,
    value: KMutableProperty1<ValueHolder<GlobalValue>, ValueHolder<LocalValue>>,
    action: (GlobalAction) -> LocalAction?
): Reducer<GlobalValue, GlobalAction> {
    return { globalValueHolder, globalAction ->
        val  localAction = action(globalAction)
        if (localAction != null) {
            val localValue = value.get(globalValueHolder)
            reducer(localValue, localAction)
            value.set(globalValueHolder, localValue)
        }
    }
}*/
