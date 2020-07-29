package com.syaremych.composable_architecture.store

import com.syaremych.composable_architecture.prelude.types.Getter
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.Prism

class Reducer1<Value, Action, Environment>(
    internal val reducer: (Value, Action, Environment) -> Reduced<Value, Action>
)

operator fun <Value, Action, Environment> Reducer1<Value, Action, Environment>.invoke(
    value: Value,
    action: Action,
    environment: Environment
) = reducer(value, action, environment)

fun <Value, Action, Environment> Reducer1<Value, Action, Environment>.combine(vararg reducers: Reducer1<Value, Action, Environment>) =
    Reducer1<Value, Action, Environment> { value, action, environment ->
        var reducedValue: Value = value
        val listOfEffects = mutableListOf<Effect<Action>>()

        for (reducer in reducers) {
            val (v, effects) = reducer(reducedValue, action, environment)
            reducedValue = v
            listOfEffects.addAll(effects)
        }
        return@Reducer1 reduced(reducedValue, listOfEffects)
    }

fun <Value,
        Action,
        Environment,
        GlobalValue,
        GlobalAction,
        GlobalEnvironment> Reducer1<Value, Action, Environment>.pullback(
    value: Lens<GlobalValue, Value>,
    action: Prism<GlobalAction, Action>,
    environment: Getter<GlobalEnvironment, Environment>
): Reducer1<GlobalValue, GlobalAction, GlobalEnvironment> {
    return Reducer1 { globalValue, globalAction, globalEnvironment ->
        val localAction = action.get(globalAction)
        if (localAction is Option.None) return@Reducer1 reduced(globalValue, noEffects())

        val localValue = value.get(globalValue)
        val localEnvironment = environment.get(globalEnvironment)

        val (reducedLocalValue, reducedLocalEffects)
                = this@pullback(localValue, localAction.value, localEnvironment)

        return@Reducer1 reduced(
            value.set(globalValue, reducedLocalValue),
            reducedLocalEffects.map { localEffect ->
                Effect<GlobalAction> { callback ->
                    localEffect.run { localAction -> callback.invoke(action.reverseGet(localAction)) }
                }
            }
        )
    }
}