package com.syaremych.composable_architecture.store

import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Prism
import kotlinx.coroutines.flow.map

interface ReducerScope<Value : Any, Action : Any, Environment : Any> {
    var state: Value
    val action: Action
    val environment: Environment
}

// TODO: Wait for Kotlin 1.4.10 fix, for some reason `fun interface` gives an invalid bytecode
//  kotlin java.lang.ClassFormatError: Bad method name at constant pool
/*fun*/ interface Reducer<Value, Action : Any, Environment : Any> {
    fun reduce(value: Value, action: Action, environment: Environment): Reduced<Value, Action>

    companion object {
        operator fun <Value, Action : Any, Environment : Any> invoke(
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

        fun <Value : Any, Action : Any, Environment : Any> of(scope: ReducerScope<Value, Action, Environment>.() -> Effect<Action>): Reducer<Value, Action, Environment> =
            object : Reducer<Value, Action, Environment>, ReducerScope<Value, Action, Environment> {
                private lateinit var _state: Value
                private lateinit var _action: Action
                private lateinit var _environment: Environment

                override var state: Value
                    get() = _state
                    set(value) {
                        _state = value
                    }
                override val action: Action
                    get() = _action
                override val environment: Environment
                    get() = _environment

                override fun reduce(
                    value: Value,
                    action: Action,
                    environment: Environment
                ): Reduced<Value, Action> {
                    _state = value
                    _action = action
                    _environment = environment
                    val effect = scope()
                    return reduced(state, effect)
                }
            }

    }
}

fun <Value : Any,
        Action : Any,
        Environment : Any> Reducer.Companion.combine(vararg reducers: Reducer<Value, Action, Environment>) =
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
        Environment : Any,
        GlobalValue : Any,
        GlobalAction : Any,
        GlobalEnvironment : Any> Reducer<Value, Action, Environment>.pullback(
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
                    println("pullback: ${ac.toString()}")
                    action.reverseGet(ac)
                }
                .eraseToEffect()
        )
    }
}

fun <Value, Action : Any, Environment : Any> Reducer.Companion.empty() =
    Reducer<Value, Action, Environment> { value, _, _ ->
        reduced(value, Effect.none())
    }

fun <Value : Any, Action : Any, Environment : Any> Reducer<Value, Action, Environment>.optional(): Reducer<Value?, Action, Environment> {
    return Reducer.invoke { value, action, environment ->
        return@invoke if (value != null) {
            this.reduce(value, action, environment)
        } else {
            reduced(value)
        }
    }
}