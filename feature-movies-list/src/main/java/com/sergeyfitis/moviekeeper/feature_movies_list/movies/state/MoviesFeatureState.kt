package com.sergeyfitis.moviekeeper.feature_movies_list.movies.state

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option

// Should contain all variables of the feature(including all its sub-states)
data class MoviesFeatureState(
    val selectedMovie: Option<Movie>,
    val movies: List<Movie>,
    val favorites: Set<Int>
) {
    companion object
}

// Should contain domain specific variables
internal data class MoviesState(
    val selectedMovie: Option<Movie>,
    val movies: List<Movie>
)

internal val MoviesFeatureState.Companion.moviesState
    get() = Lens<MoviesFeatureState, MoviesState>(
        get = { featureState -> MoviesState(Option.None, featureState.movies) },
        set = { featureState, viewState ->
            featureState.copy(
                selectedMovie = viewState.selectedMovie,
                movies = viewState.movies
            )
        }
    )