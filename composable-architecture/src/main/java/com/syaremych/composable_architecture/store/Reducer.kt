package com.syaremych.composable_architecture.store

import android.util.Log
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Prism
import kotlinx.coroutines.flow.map
// TODO: Wait for Kotlin 1.4.10 fix, for some reason `fun interface` gives an invalid bytecode
//  kotlin java.lang.ClassFormatError: Bad method name at constant pool
/*fun*/ interface Reducer<Value : Any, Action : Any, Environment> {
    fun reduce(value: Value, action: Action, environment: Environment): Reduced<Value, Action>

    companion object {
        operator fun <Value : Any, Action : Any, Environment>  invoke(
            reducer: (Value, Action, Environment) -> Reduced<Value, Action>
        ) = object : Reducer<Value, Action, Environment> {
            override fun reduce(
                value: Value,
                action: Action,
                environment: Environment
            ): Reduced<Value, Action> {
                return reducer(value, action, environment)
            }
        }
    }
}

fun <Value : Any,
        Action : Any,
        Environment> Reducer.Companion.combine(vararg reducers: Reducer<Value, Action, Environment>) =
    Reducer<Value, Action, Environment> { value, action, environment ->

        var reducedValue: Value = value
        val reducedEffects = mutableListOf<Effect<Action>>()

        for (reducer in reducers) {
            val (newValue, effect) = reducer.reduce(reducedValue, action, environment)
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
        val localAction = action.get(globalAction) ?: return@Reducer reduced(globalValue)

        val localValue = value.get(globalValue)
        val localEnvironment = environment.invoke(globalEnvironment)

        val (reducedLocalValue, reducedLocalEffect)
                = this@pullback.reduce(localValue, localAction, localEnvironment)

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