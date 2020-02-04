package com.sergeyfitis.moviekeeper.prelude.types

import com.sergeyfitis.moviekeeper.prelude.pipe
import com.sergeyfitis.moviekeeper.prelude.withA

interface Lens<A, B> {
    fun get(a: A): B
    fun set(a: A, b: B): A

    fun modify(a: A, f: (B) -> B): A {
        val b = get(a)
        return set(a, f(b))
    }

    fun lift(f: (B) -> B): (A) -> A {
        return { a -> modify(a, f) }
    }

    infix fun <C> pipe(g: Lens<B, C>): Lens<A, C> {
        return Lens(
            get = { a ->
                withA(
                    a,
                    ::get pipe g::get
                ) /*g.get(get(a))*/
            },
            set = { a, c -> set(a, g.set(get(a), c)) }
        )
    }
    operator fun <C> plus(g: Lens<B, C>): Lens<A, C> = this pipe g
    infix fun <C> pipe(g: Getter<B, C>): Getter<A, C> = asGetter() pipe g

    fun asGetter() = Getter(this::get)

    companion object {
        operator fun <A, B> invoke(
            get: (A) -> B,
            set: (A, B) -> A
        ) = object : Lens<A, B> {
            override fun get(a: A): B = get(a)
            override fun set(a: A, b: B): A = set(a, b)
        }
    }
}