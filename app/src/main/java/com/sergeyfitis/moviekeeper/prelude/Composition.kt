package com.sergeyfitis.moviekeeper.prelude

import com.sergeyfitis.moviekeeper.statemanagement.store.Effect
import com.sergeyfitis.moviekeeper.statemanagement.store.Reducer
import com.sergeyfitis.moviekeeper.statemanagement.store.noEffects
import com.sergeyfitis.moviekeeper.statemanagement.store.reduced

fun <Value, Action> combine(
    vararg reducers: Reducer<Value, Action>
): Reducer<Value, Action> {
    return { value, action ->
        var reducedValue: Value = value
        val listOfEffects = mutableListOf<Effect<Action>>()

        for (reducer in reducers) {
            val (v, effects) = reducer(reducedValue, action)
            reducedValue = v
            listOfEffects.addAll(effects)
        }
        reducedValue to listOfEffects
    }
}

fun <LocalValue, GlobalValue, LocalAction, GlobalAction> pullback(
    reducer: Reducer<LocalValue, LocalAction>,
    valueGet: (GlobalValue) -> LocalValue,
    valueSet: (GlobalValue, LocalValue) -> GlobalValue,
    toLocalAction: (GlobalAction) -> LocalAction?,
    toGlobalAction: (LocalAction?) -> GlobalAction?
): Reducer<GlobalValue, GlobalAction> {
    return globalReducer@{ globalValue, globalAction ->
        val localAction = toLocalAction(globalAction)
            ?: return@globalReducer reduced(globalValue, noEffects())
        val localValue = valueGet(globalValue)
        val (reducedLocalValue, reducedLocalEffects) = reducer(localValue, localAction)
        return@globalReducer reduced(
            value = valueSet(globalValue, reducedLocalValue),
            effects = reducedLocalEffects.map { localEffect ->
                Effect<GlobalAction> { callback ->
                    localEffect.run { localAction -> toGlobalAction(localAction)?.let(callback) }
                }
            }
        )
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
