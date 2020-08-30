@file:Suppress("NOTHING_TO_INLINE", "unused", "UNUSED_PARAMETER")

package com.syaremych.composable_architecture.prelude

import kotlin.reflect.KFunction2
import kotlin.reflect.KFunction3
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

inline fun <A> absurd(a: A): Nothing {
    throw RuntimeException(
        """
            |It should never happen. Impossible scenario.
            |If this happened, then you are doing something wrong
        """.trimMargin())
}

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
        val (innerA, bc) = abc
        Triple(innerA, bc.first, bc.second)
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

inline fun <A> identity(a: A) = a

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

// <>
inline infix fun (() -> Unit)?.concat(crossinline g: () -> Unit): () -> Unit {
    return {
        this?.invoke()
        g.invoke()
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

@JvmName("propFunc2")
fun <Root, Value> prop(kf: KFunction2<Root, Value, Root>): (() -> Value) -> (Root) -> Root {
    return { update -> { root -> kf.invoke(root, update()) } }
}

@JvmName("propFunc3")
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
