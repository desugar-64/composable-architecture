package com.sergeyfitis.moviekeeper.prelude

fun <Value, Action> combine(
    f: (Value, Action) -> Unit,
    g: (Value, Action) -> Unit
): (Value, Action) -> Unit {
    return { value, action ->
        f(value, action)
        g(value, action)
    }
}