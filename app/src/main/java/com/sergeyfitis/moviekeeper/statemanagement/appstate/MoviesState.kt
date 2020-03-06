package com.sergeyfitis.moviekeeper.statemanagement.appstate

import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.prelude.types.Lens
import com.sergeyfitis.moviekeeper.prelude.types.Option

data class MoviesState(
    val selectedMovie: Option<Movie>,
    val movies: List<Movie>
)

data class MoviesViewState(
    val moviesState: MoviesState
) {
    companion object
}

val MoviesViewState.Companion.moviesStateLens
    get() = Lens<MoviesViewState, MoviesState>(
        get = { moviesViewState -> moviesViewState.moviesState },
        set = { moviesViewState, moviesState ->
            moviesViewState.copy(moviesState = moviesState)
        }
    )