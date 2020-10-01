package com.syaremych.composable_architecture.store

import android.util.Log
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Prism
import kotlinx.coroutines.flow.map

open class Reducer<Value, Action, Environment>(
    internal val reduce: (Value, Action, Environment) -> Reduced<Value, Action>
) where Value : Any, Action : Any {
    companion object
}

operator fun <Value, Action, Environment> Reducer<Value, Action, Environment>.invoke(
    value: Value,
    action: Action,
    environment: Environment
) where Value : Any, Action : Any = reduce(value, action, environment)

fun <Value : Any,
        Action : Any,
        Environment> Reducer.Companion.combine(vararg reducers: Reducer<Value, Action, Environment>) =
    Reducer<Value, Action, Environment> { value, action, environment ->

        var reducedValue: Value = value
        val reducedEffects = mutableListOf<Effect<Action>>()

        for (reducer in reducers) {
            val (newValue, effect) = reducer(reducedValue, action, environment)
            reducedValue = newValue
            reducedEffects.add(effect)
        }
        return@Reducer reduced(reducedValue, Effect.concat(reducedEffects))
    }

fun <Value : Any,
        Action : Any,
        Environment,
        GlobalValue : Any,
        GlobalAction : Any,
        GlobalEnvironment> Reducer<Value, Action, Environment>.pullback(
    value: Lens<GlobalValue, Value>,
    action: Prism<GlobalAction, Action>,
    environment: (GlobalEnvironment) -> Environment
): Reducer<GlobalValue, GlobalAction, GlobalEnvironment> {
    return Reducer { globalValue, globalAction, globalEnvironment ->
        val localAction = action.get(globalAction)
        if (localAction.isEmpty) return@Reducer reduced(globalValue)

        val localValue = value.get(globalValue)
        val localEnvironment = environment.invoke(globalEnvironment)

        val (reducedLocalValue, reducedLocalEffect)
                = this@pullback(localValue, localAction.value, localEnvironment)

        return@Reducer reduced(
            value.set(globalValue, reducedLocalValue),
            reducedLocalEffect
                .map { ac ->
                    Log.d("pullback", ac.toString())
                    action.reverseGet(ac)
                }
                .eraseToEffect()
        )
    }
}

fun <Value : Any, Action : Any, Environment> Reducer.Companion.empty() =
    Reducer<Value, Action, Environment> { value, _, _ ->
        reduced(value, Effect.none())
    }