@file:Suppress("NOTHING_TO_INLINE")

package com.sergeyfitis.moviekeeper.prelude

import com.sergeyfitis.moviekeeper.statemanagement.store.Effect
import com.sergeyfitis.moviekeeper.statemanagement.store.Reducer
import com.sergeyfitis.moviekeeper.statemanagement.store.noEffects
import com.sergeyfitis.moviekeeper.statemanagement.store.reduced
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

inline fun <A> id(): (A) -> A = { it }

// <>
inline fun <A> concat(crossinline f: (A) -> Unit, crossinline g: (A) -> Unit): (A) -> Unit {
    return { a ->
        f(a)
        g(a)
    }
}
// <>
inline fun <A> concat(crossinline f: (A) -> Unit, crossinline g: (A) -> Unit, vararg fs: (A) -> Unit): (A) -> Unit {
    return { a ->
        f(a)
        g(a)
        fs.forEach { it(a) }
    }
}


// >>>
@JvmName("pipeInfix")
inline infix fun <A, B, C> ((A) -> B).pipe(crossinline g: (B) -> C): (A) -> C {
    return { a -> g(this(a)) }
}
// >>>
inline fun <A, B, C, D> pipe(
    crossinline f: (A) -> B,
    crossinline g: (B) -> C,
    crossinline h: (C) -> D
): (A) -> D {
    return { a -> h(g(f(a))) }
}
// >>>
inline fun <A, B, C> pipe(crossinline f: (A) -> B, crossinline g: (B) -> C): (A) -> C {
    return { a -> g(f(a)) }
}
// |>
inline fun <A, B> withA(a: A, f: (A) -> B): B = f(a)


inline fun <Root, Value> prop(
    kp: KProperty1<Root, Value>,
    crossinline set: (Value, Root) -> Root
): ((Value) -> Value) -> (Root) -> Root =
    update@{ update ->
        return@update root@{ root ->
            val value: Value = update(kp.get(root))
            return@root set(value, root)
        }
    }

inline fun <Root, Value> prop(kp: KMutableProperty1<Root, Value>): ((Value) -> Value) -> (Root) -> Root =
    update@{ update ->
        return@update root@{ root ->
            kp.set(root, update(kp.get(root)))
            return@root root
        }
    }

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
