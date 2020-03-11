package com.syaremych.composable_architecture.prelude.types

import com.syaremych.composable_architecture.prelude.id

sealed class Option<out A> {
    abstract val isEmpty: Boolean
    abstract val value: A
    object None : Option<Nothing>() {
        override val isEmpty: Boolean = true
        override val value: Nothing
            get() = throw RuntimeException("Nothing to unwrap")
    }
    data class Some<out A>(override val value: A) : Option<A>() {
        override val isEmpty: Boolean = false
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

inline fun <T> Option<T>.getOrElse(default: () -> T): T = fold(default, ::id)
inline fun <T> Option<T>.getOrThrow(): T = fold({ throw RuntimeException("Nothing to unpack") }, ::id)
fun <T> T?.toOption(): Option<T> = Option.ofNullable(this)