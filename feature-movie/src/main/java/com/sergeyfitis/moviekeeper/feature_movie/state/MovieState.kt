package com.sergeyfitis.moviekeeper.feature_movie.state

import androidx.compose.runtime.Immutable
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.syaremych.composable_architecture.prelude.absurd
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.prelude.types.*

data class MovieFeatureState(
    val selectedMovie: MovieDTO,
    val favoriteMovies: Set<MovieDTO>

) {
    companion object
}

val MovieFeatureState.Companion.movieState
    get() = Lens<Option<MovieFeatureState>, Option<MovieState>>(
        get = { movieFeatureState ->
            movieFeatureState.flatMap { featureState ->
                MovieState(
                    featureState.selectedMovie,
                    featureState.favoriteMovies.contains(featureState.selectedMovie)
                ).toOption()
            }
        },
        set = { movieFeatureState, viewState ->
            movieFeatureState.rmap { featureState ->
                featureState.copy(favoriteMovies = if (viewState.fold({ false }, { it.isFavorite }))
                    featureState
                        .favoriteMovies
                        .toMutableSet()
                        .apply { add(viewState.fold(::absurd, ::identity).movie) }
                else
                    featureState
                        .favoriteMovies
                        .toMutableSet()
                        .apply {
                            if (viewState is Either.Right) remove(
                                viewState.fold(
                                    ::absurd,
                                    ::identity
                                ).movie
                            )
                        }
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