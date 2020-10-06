package com.sergeyfitis.moviekeeper.feature_movie.state

import androidx.compose.runtime.Immutable
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.Option

data class MovieFeatureState(
    val selectedMovie: MovieDTO,
    val favoriteMovies: Set<MovieDTO>

) {
    companion object
}

val MovieFeatureState.Companion.movieState
    get() = Lens<Option<MovieFeatureState>, Option<MovieState>>(
        get = { movieFeatureState ->
            movieFeatureState.map { featureState ->
                MovieState(
                    featureState.selectedMovie,
                    featureState.favoriteMovies.contains(featureState.selectedMovie)
                )
            }
        },
        set = { movieFeatureState, viewState ->
            movieFeatureState.map { featureState ->
                featureState.copy(favoriteMovies = if (viewState.fold({ false }, { it.isFavorite }))
                    featureState
                        .favoriteMovies
                        .toMutableSet()
                        .apply { if (!viewState.isEmpty) add(viewState.value.movie) }
                else
                    featureState
                        .favoriteMovies
                        .toMutableSet()
                        .apply { if (!viewState.isEmpty) remove(viewState.value.movie) }
                )
            }
        }
    )

@Immutable
data class MovieState(
    val movie: MovieDTO,
    val isFavorite: Boolean
) {
    companion object
}