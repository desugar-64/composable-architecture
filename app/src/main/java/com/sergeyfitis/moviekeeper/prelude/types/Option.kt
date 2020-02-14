package com.sergeyfitis.moviekeeper.prelude.types

import com.sergeyfitis.moviekeeper.prelude.id

sealed class Option<out A> {
    object None : Option<Nothing>()
    data class Some<out A>(val value: A) : Option<A>()

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
    }
}

inline fun <T> Option<T>.getOrElse(default: () -> T): T = fold({ default() }, ::id)
fun <T> T?.toOption(): Option<T> = Option.ofNullable(this)