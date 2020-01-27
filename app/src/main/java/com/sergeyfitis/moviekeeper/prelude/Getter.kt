package com.sergeyfitis.moviekeeper.prelude

interface Getter<A, B> {
    fun get(a: A): B

    infix fun <C> pipe(g: Getter<B, C>) = Getter(this::get pipe g::get)
    operator fun <C> plus(g: Getter<B, C>) = this pipe g
    infix fun <C> pipe(g: Lens<B, C>): Getter<A, C> = Getter(this::get pipe g::get)
    operator fun <C> plus(g: Lens<B, C>) = this pipe g

    companion object {
        operator fun <A, B> invoke(get: (a: A) -> B): Getter<A, B> {
            return object : Getter<A, B> {
                override fun get(a: A) = get(a)
            }
        }
    }
}