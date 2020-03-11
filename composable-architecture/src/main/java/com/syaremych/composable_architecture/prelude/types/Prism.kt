package com.syaremych.composable_architecture.prelude.types

/*
* A [Prism] is a loss less invertible optic that can look into a structure and optionally find its focus.
* Mostly used for finding a focus that is only present under certain conditions i.e. list head Prism<List<Int>, Int>
* */
interface Prism<A, B> {
    fun get(a: A): Option<B>
    fun reverseGet(b: B): A

    fun modify(a: A, f: (B) -> B): A {
        val b = get(a)
        return b.fold({ a }, { reverseGet(f(it)) })
    }

    fun lift(f: (B) -> B): (A) -> A {
        return { a -> modify(a, f) }
    }

    infix fun <C> pipe(g: Prism<B, C>): Prism<A, C> {
        return Prism(
            get = { a -> this.get(a).flatMap(g::get) },
            reverseGet = { c -> this.reverseGet(g.reverseGet(c)) }
        )
    }

    operator fun <C> plus(g: Prism<B, C>): Prism<A, C> = this pipe g

    companion object {
        operator fun <A, B> invoke(get: (A) -> Option<B>, reverseGet: (B) -> A) =
            object : Prism<A, B> {
                override fun get(a: A) = get(a)
                override fun reverseGet(b: B) = reverseGet(b)
            }
    }
}