package com.sergeyfitis.moviekeeper.feature_movie.state

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.syaremych.composable_architecture.prelude.types.Lens

data class MovieFeatureState(
    val selectedMovie: Movie,
    val movieState: MovieState,
    val favoriteMovies: List<Movie>
) {
    companion object
}

val MovieFeatureState.Companion.movieState
    get() = Lens<MovieFeatureState, MovieState>(
        get = { it.movieState },
        set = { movieViewState, movieState -> movieViewState.copy(movieState = movieState) }
    )

data class MovieState(
    val movie: Movie,
    val isFavorite: Boolean
) {
    companion object
}