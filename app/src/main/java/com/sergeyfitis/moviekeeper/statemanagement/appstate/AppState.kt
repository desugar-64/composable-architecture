package com.sergeyfitis.moviekeeper.statemanagement.appstate

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieFeatureState
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.toOption

data class AppState(
    val moviesFeatureState: MoviesFeatureState,
    val movieFeatureState: Option<MovieFeatureState>,
    val favoriteMovies: Set<Movie>,
    val movieState: Option<MovieState>
) {
    companion object {
        fun initial() = AppState(
            moviesFeatureState = MoviesFeatureState(Option.empty(), emptyList(), emptySet()),
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

val AppState.Companion.movieFeatureState: Lens<AppState, Option<MovieFeatureState>>
    get() = Lens(
        get = { appState ->
            when (val movie = appState.moviesFeatureState.selectedMovie) {
                Option.None -> Option.empty()
                is Option.Some -> MovieFeatureState(
                    selectedMovie = movie.value,
                    favoriteMovies = appState
                        .movieFeatureState
                        .map(MovieFeatureState::favoriteMovies)
                        .fold(::emptySet, ::identity)
                ).toOption()

            }
        },
        set = { appState, movieFeatureState ->
            appState.copy(movieFeatureState = movieFeatureState)
        }
    )