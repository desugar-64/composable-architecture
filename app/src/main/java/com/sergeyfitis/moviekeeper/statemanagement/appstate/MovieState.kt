package com.sergeyfitis.moviekeeper.statemanagement.appstate

import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.prelude.types.Either

data class MovieState(
    val movie: Either<Unit, Movie>,
    val isFavorite: Boolean
) {
    companion object
}