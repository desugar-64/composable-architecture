package com.sergeyfitis.moviekeeper.feature_movies_list.movies.state

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.syaremych.composable_architecture.prelude.types.Getter
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option

data class MoviesFeatureState(
    val movies: List<Movie>
) {
    companion object
}

internal data class ViewState(
    val selectedMovie: Option<Movie>,
    val movies: List<Movie>
)

internal val MoviesFeatureState.Companion.moviesState
    get() = Lens<MoviesFeatureState, ViewState>(
        get = { featureState -> ViewState(Option.None, featureState.movies) },
        set = { featureState, viewState -> featureState.copy(movies = viewState.movies) }
    )