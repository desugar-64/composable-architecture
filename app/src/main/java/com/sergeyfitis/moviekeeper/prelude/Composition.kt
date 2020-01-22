@file:Suppress("NOTHING_TO_INLINE")

package com.sergeyfitis.moviekeeper.prelude

import com.sergeyfitis.moviekeeper.statemanagement.store.Effect
import com.sergeyfitis.moviekeeper.statemanagement.store.Reducer
import com.sergeyfitis.moviekeeper.statemanagement.store.noEffects
import com.sergeyfitis.moviekeeper.statemanagement.store.reduced
import kotlin.reflect.KFunction3
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1


inline fun <A, B, C, D> zip3(crossinline f: (A, B, C) -> D): (A?, B?, C?) -> D? {
    return { a, b, c ->
        zip3(a, b, c)?.map { abc -> f(abc.first, abc.second, abc.third) } }
}

inline fun <A, B, C> zip2(crossinline f: (A, B) -> C): (A?, B?) -> C? {
    return { a, b -> zip2(a, b)?.map { ab -> f(ab.first, ab.second) } }
}

inline fun <A, B, C, D> Triple<A, B, C>.map(f: (Triple<A, B, C>) -> D): D {
    return f(this)
}

inline fun <A, B, C> Pair<A, B>.map(f: (Pair<A, B>) -> C): C {
    return f(this)
}

fun <A, B, C> zip3(a: A?, b: B?, c: C?): Triple<A, B, C>? {
    return zip2(a, zip2(b, c))?.map { abc: Pair<A, Pair<B, C>>? ->
        abc ?: return@map null
        val (a, bc) = abc
        Triple(a, bc.first, bc.second)
    }
}

fun <A, B> zip2(a: A?, b: B?): Pair<A, B>? {
    if (a == null || b == null) return null
    return a to b
}

inline infix fun <A, B> ((A) -> B).map(crossinline f: (A) -> B): (A) -> B {
    return { a: A -> f(a) }
}

inline fun <A, B> map(crossinline f: (A) -> B): (A) -> B {
    return { a: A -> f(a) }
}

inline fun <A> id(a: A) = a

// <>
inline fun <A> concat(crossinline f: (A) -> Unit, crossinline g: (A) -> Unit): (A) -> Unit {
    return { a ->
        f(a)
        g(a)
    }
}

// <>
inline fun <A> concat(
    crossinline f: (A) -> Unit,
    crossinline g: (A) -> Unit,
    vararg fs: (A) -> Unit
): (A) -> Unit {
    return { a ->
        f(a)
        g(a)
        fs.forEach { it(a) }
    }
}


// >>>
inline infix operator fun <A, B, C> ((A) -> B).plus(crossinline g: (B) -> C): (A) -> C {
    return { a -> g(this(a)) }
}

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

// <<<
/*
* let nested = ((1, true), "Swift")
* nested
*  |> (first <<< second) { !$0 }
* */
inline fun <A, B, C> pipeBackward(crossinline f: (B) -> C, crossinline g: (A) -> B): (A) -> C {
    return  { a -> f(g(a)) }
}

@JvmName("pipeBackwardI")
inline infix fun <A, B, C> ((B) -> C).pipeBackward(crossinline g: (A) -> B): (A) -> C {
    return  { a -> this(g(a)) }
}

// >>>
inline fun <A, B, C> pipe(crossinline f: (A) -> B, crossinline g: (B) -> C): (A) -> C {
    return { a -> g(f(a)) }
}

// |>
@JvmName("withAInfix")
inline infix fun <A, B> A.withA(f: (A) -> B): B = f(this)
inline fun <A, B> withA(a: A, f: (A) -> B): B = f(a)

fun <Root, Value> combining(
    f: (Root) -> Value,
    by: (Value, Value) -> Value
): (Value, Root) -> Value {
    return { value, root -> by(value, f(root)) }
}

fun <Root, Value0, Value1> prop(kf: KFunction3<Root, Value0, Value1, *>): (() -> Pair<Value0, Value1>) -> (Root) -> Root {
    return { update ->
        { root ->
            val (value0, value1) = update()
            kf.invoke(root, value0, value1)
            root
        }
    }
}

inline fun <Root, Value> get(kp: KProperty1<Root, Value>): (Root) -> Value = kp::get

inline fun <Root, Value> prop(
    kp: KProperty1<Root, Value>,
    crossinline set: (Value, Root) -> Root = { _, root -> root }
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
    toLocalAction: GlobalAction.() -> LocalAction?,
    toGlobalAction: LocalAction?.() -> GlobalAction?
): Reducer<GlobalValue, GlobalAction> {
    return globalReducer@{ globalValue, globalAction ->
        val localAction = globalAction.let(toLocalAction)
            ?: return@globalReducer reduced(globalValue, noEffects())
        val localValue = valueGet(globalValue)
        val (reducedLocalValue, reducedLocalEffects)
                = reducer(localValue, localAction)
        return@globalReducer reduced(
            value = valueSet(globalValue, reducedLocalValue),
            effects = reducedLocalEffects.map { localEffect ->
                Effect<GlobalAction> { callback ->
                    localEffect.run { localAction ->
                        withA(localAction, toGlobalAction)?.let(callback) }
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
