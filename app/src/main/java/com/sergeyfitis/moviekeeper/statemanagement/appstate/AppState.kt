package com.sergeyfitis.moviekeeper.statemanagement.appstate

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieFeatureState
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.getOrThrow
import com.syaremych.composable_architecture.prelude.types.toOption

data class AppState(
    val moviesFeatureState: MoviesFeatureState,
    val movieFeatureState: Option<MovieFeatureState>,
    val favoriteMovies: Set<Movie>,
    val movieState: Option<MovieState>
) {
    companion object {
        fun initial() = AppState(
            moviesFeatureState = MoviesFeatureState(emptyList()),
            movieFeatureState = Option.empty(),
            favoriteMovies = emptySet(),
            movieState = Option.empty()
        )
    }
}

val AppState.Companion.moviesFeatureState: Lens<AppState, MoviesFeatureState>
    get() = Lens(
        get = AppState::moviesFeatureState,
        set = { appState, moviesFeatureState ->
            appState.copy(moviesFeatureState = moviesFeatureState)
        }
    )

val AppState.Companion.movieFeatureState: Lens<AppState, MovieFeatureState>
    get() = Lens(
        get = { appState -> appState.movieFeatureState.getOrThrow() },
        set = { appState, movieFeatureState ->
            appState.copy(movieFeatureState = movieFeatureState.toOption())
        }
    )