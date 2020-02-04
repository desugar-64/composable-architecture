package com.sergeyfitis.moviekeeper.prelude.types


sealed class Either<out L, out R> {

    data class Left<L>(val value: L) : Either<L, Nothing>()
    data class Right<R>(val value: R) : Either<Nothing, R>()

    fun <C> fold(ifLeft: (L) -> C, ifRight: (R) -> C): C =
        when (this) {
            is Left -> ifLeft(value)
            is Right -> ifRight(value)
        }

    companion object
}

fun <T> T.left() = Either.Left(this)
fun <T> T.right() = Either.Right(this)

fun <L, R> Either<L, R>.swap(): Either<R, L> = fold({ Either.Right(it) }, { Either.Left(it) })

fun <L, R1, R2> Either<L, R1>.flatMap(f: (R1) -> Either<L, R2>): Either<L, R2> =
    fold({ Either.Left(it) }, f)

fun <L, R1, R2> Either<L, R1>.rmap(f: (R1) -> R2): Either<L, R2> =
    fold({ Either.Left(it) }, { Either.Right(f(it)) })

fun <L1, L2, R> Either<L1, R>.lmap(f: (L1) -> L2): Either<L2, R> =
    swap().rmap(f).swap()

fun <T> Either.Companion.ofNullable(value: T?): Either<Unit, T> =
    value?.let { Either.Right(it) } ?: Either.Left(Unit)

inline fun <R> Either.Companion.recover(f: () -> R): Either<Throwable, R> =
    try {
        Either.Right(f())
    } catch (t: Throwable) {
        Either.Left(t)
    }