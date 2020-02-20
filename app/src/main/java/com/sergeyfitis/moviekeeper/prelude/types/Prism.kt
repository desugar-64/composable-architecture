package com.sergeyfitis.moviekeeper.prelude.types

/*
* A [Prism] is a loss less invertible optic that can look into a structure and optionally find its focus.
* Mostly used for finding a focus that is only present under certain conditions i.e. list head Prism<List<Int>, Int>
* */
interface Prism<A, B> {
    fun getOption(a: A): Option<B>
    fun reverseGet(b: B): A

    companion object {
        operator fun <A, B> invoke(getOption: (A) -> Option<B>, reverseGet: (B) -> A) =
            object : Prism<A, B> {
                override fun getOption(a: A) = getOption(a)
                override fun reverseGet(b: B) = reverseGet(b)
            }
    }
}