package com.sergeyfitis.moviekeeper.statemanagement.appstate

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.syaremych.composable_architecture.prelude.types.Lens

data class MovieViewState(
    val selectedMovie: Movie,
    val movieState: MovieState
) {
    companion object
}

val MovieViewState.Companion.movieStateLens
    get() = Lens<MovieViewState, MovieState>(
        get = { it.movieState },
        set = { movieViewState, movieState -> movieViewState.copy(movieState = movieState) }
    )

data class MovieState(
    val movie: Movie,
    val isFavorite: Boolean
) {
    companion object
}