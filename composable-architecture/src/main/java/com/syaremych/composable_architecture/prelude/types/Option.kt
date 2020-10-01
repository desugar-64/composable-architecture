@file:Suppress("NOTHING_TO_INLINE")

package com.syaremych.composable_architecture.prelude.types

import com.syaremych.composable_architecture.prelude.identity

sealed class Option<out A> {
    abstract val isEmpty: Boolean
    abstract val value: A

    object None : Option<Nothing>() {
        override val isEmpty: Boolean = true
        override val value: Nothing
            get() = throw RuntimeException("Nothing to unwrap")
        override fun toString() = "Option.None"
    }

    data class Some<out A>(override val value: A) : Option<A>() {
        override val isEmpty: Boolean = false
        override fun toString() = "Option.Some(value=$value)"
    }

    inline infix fun <B> map(f: (A) -> B): Option<B> =
        flatMap { a -> Some(f(a)) }

    inline infix fun <B> flatMap(f: (A) -> Option<B>): Option<B> =
        fold({ empty() }, f)

    inline fun <B> fold(ifEmpty: () -> B, ifSome: (A) -> B): B = when (this) {
        is None -> ifEmpty()
        is Some<A> -> ifSome(value)
    }

    inline infix fun <B> mapNotNull(f: (A) -> B?): Option<B> =
        flatMap { a -> ofNullable(f(a)) }

    companion object {
        operator fun <A> invoke(a: A): Option<A> = Some(a)
        fun <A> just(a: A): Option<A> = Some(a)
        fun <A> empty(): Option<A> = None
        fun <A> ofNullable(a: A?): Option<A> = if (a != null) Some(a) else None
        inline fun <A> recover(getA: () -> A) =
            Either.recover(getA).fold({ empty<A>() }, ::just)
    }
}

inline fun <T> Option<T>.getOrElse(default: () -> T): T = fold(default, ::identity)
inline fun <T> Option<T>.getOrThrow(): T = fold({
    throw RuntimeException(
        """
            |Nothing to unpack. 
            |You must put something in order to get something back.
        """.trimMargin()
    )
}, ::identity)

fun <T> T?.toOption(): Option<T> = Option.ofNullable(this)

fun <K, V> Map<K, V>.getOption(key: K): Option<V> = this[key].toOption()