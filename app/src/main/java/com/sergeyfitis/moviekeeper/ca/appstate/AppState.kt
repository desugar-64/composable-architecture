package com.sergeyfitis.moviekeeper.ca.appstate

import com.sergeyfitis.moviekeeper.data.models.dto.GenreDTO
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieFeatureState
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass.MoviesFeatureState
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.orNull
import com.syaremych.composable_architecture.prelude.types.toOption

data class AppState(
    val moviesFeatureState: MoviesFeatureState,
    val movieFeatureState: MovieFeatureState?,
    val favoriteFeatureState: FavoriteFeatureState,
    val movieState: MovieState?,
    val genres: Map<Int, GenreDTO>
) {
    companion object {
        fun initial() = AppState(
            moviesFeatureState = MoviesFeatureState.init(),
            movieFeatureState = null,
            favoriteFeatureState = FavoriteFeatureState.init(),
            movieState = null,
            genres = emptyMap()
        )
    }
}

val AppState.Companion.moviesFeatureState: Lens<AppState, MoviesFeatureState>
    get() = Lens(
        get = { appState -> appState.moviesFeatureState.copy(genres = appState.genres) }
    ) { appState, moviesFeatureState ->
        appState.copy(
            moviesFeatureState = moviesFeatureState,
            favoriteFeatureState = appState.favoriteFeatureState.copy(
                movies = moviesFeatureState.movies,
            )
        )
    }

val AppState.Companion.movieFeatureState: Lens<AppState, Option<MovieFeatureState>>
    get() = Lens(
        get = { appState ->
            appState.moviesFeatureState.selectedMovie?.let { movie ->
                MovieFeatureState(
                    selectedMovie = movie,
                    favoriteMovies = appState.movieFeatureState?.favoriteMovies ?: emptySet()
                )
            }.toOption()
        },
        set = { appState, movieFeatureState ->
            appState.copy(movieFeatureState = movieFeatureState.orNull)
        }
    )

val AppState.Companion.favoriteFeatureState: Lens<AppState, FavoriteFeatureState>
    get() = Lens(
        get = AppState::favoriteFeatureState,
        set = { appState, favoriteFeatureState ->
            appState.copy(favoriteFeatureState = favoriteFeatureState)
        }
    )

internal val AppState.Companion.identityLens: Lens<AppState, AppState>
    get() = Lens(
        get = ::identity,
        set = { appState, state ->
            appState.copy(
                genres = state.genres,
                moviesFeatureState = appState.moviesFeatureState.copy(genres = appState.genres)
            )
        }
    )