package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass

import com.sergeyfitis.moviekeeper.data.models.MovieDTO
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option

// Should contain all variables of the feature(including all its sub-states)
data class MoviesFeatureState(
    val selectedMovie: Option<MovieDTO>,
    val movies: Map<Int, MovieDTO>,
    val nowPlaying: Set<Int>,
    val upcoming: Set<Int>,
    val topRated: Set<Int>,
) {
    companion object {
        fun init() = MoviesFeatureState(
            selectedMovie = Option.empty(),
            movies = emptyMap(),
            nowPlaying = emptySet(),
            upcoming = emptySet(),
            topRated = emptySet()
        )
    }
}

// Should contain domain specific variables
internal data class MoviesState(
    val selectedMovie: Option<MovieDTO>,
    val movies: Map<Int, MovieDTO>,
    val nowPlaying: Set<Int>,
    val upcoming: Set<Int>,
    val topRated: Set<Int>,
)

internal val MoviesFeatureState.Companion.moviesState
    get() = Lens<MoviesFeatureState, MoviesState>(
        get = { featureState ->
            MoviesState(
                selectedMovie = Option.None,
                movies = featureState.movies,
                nowPlaying = featureState.nowPlaying,
                upcoming = featureState.upcoming,
                topRated = featureState.topRated
            )
        },
        set = { featureState, viewState ->
            featureState.copy(
                selectedMovie = viewState.selectedMovie,
                movies = viewState.movies,
                nowPlaying = viewState.nowPlaying,
                upcoming = viewState.upcoming,
                topRated = viewState.topRated,
            )
        }
    )