package com.syaremych.composable_architecture.store

import com.syaremych.composable_architecture.prelude.types.Getter
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.Prism

class Reducer<Value, Action, Environment>(
    internal val reducer: (Value, Action, Environment) -> Reduced<Value, Action>
) where Value : Any, Action : Any

operator fun <Value, Action, Environment> Reducer<Value, Action, Environment>.invoke(
    value: Value,
    action: Action,
    environment: Environment
) where Value : Any, Action : Any = reducer(value, action, environment)

fun <Value : Any,
        Action : Any,
        Environment> Reducer<Value, Action, Environment>.combine(vararg reducers: Reducer<Value, Action, Environment>) =
    Reducer<Value, Action, Environment> { value, action, environment ->
        var reducedValue: Value = value
        val listOfEffects = mutableListOf<Effect<Action>>()

        for (reducer in reducers) {
            val (v, effects) = reducer(reducedValue, action, environment)
            reducedValue = v
            listOfEffects.addAll(effects)
        }
        return@Reducer reduced(reducedValue, listOfEffects)
    }

fun <Value : Any,
        Action : Any,
        Environment,
        GlobalValue : Any,
        GlobalAction : Any,
        GlobalEnvironment> Reducer<Value, Action, Environment>.pullback(
    value: Lens<GlobalValue, Value>,
    action: Prism<GlobalAction, Action>,
    environment: Getter<GlobalEnvironment, Environment>
): Reducer<GlobalValue, GlobalAction, GlobalEnvironment> {
    return Reducer { globalValue, globalAction, globalEnvironment ->
        val localAction = action.get(globalAction)
        if (localAction is Option.None) return@Reducer reduced(globalValue, noEffects())

        val localValue = value.get(globalValue)
        val localEnvironment = environment.get(globalEnvironment)

        val (reducedLocalValue, reducedLocalEffects)
                = this@pullback(localValue, localAction.value, localEnvironment)

        return@Reducer reduced(
            value.set(globalValue, reducedLocalValue),
            reducedLocalEffects.map { localEffect ->
                Effect<GlobalAction> { callback ->
                    localEffect.run { localAction -> callback.invoke(action.reverseGet(localAction)) }
                }
            }
        )
    }
}