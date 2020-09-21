package com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.syaremych.composable_architecture.prelude.types.Lens

data class FavoriteFeatureState(
    val favoriteMovies: Set<Int>,
    val movies: Map<Int, Movie>
) {
    companion object {
        fun init() = FavoriteFeatureState(emptySet(), emptyMap())
    }
}

internal data class FavoriteState(
    val favoriteMovies: Set<Int>,
    val movies: Map<Int, Movie>
)

internal val FavoriteFeatureState.Companion.favoriteMoviesState: Lens<FavoriteFeatureState, FavoriteState>
    get() = Lens(
        get = { featureState ->
            FavoriteState(featureState.favoriteMovies, featureState.movies)
        },
        set = { featureState, state ->
            featureState.copy(favoriteMovies = state.favoriteMovies)
        }
    )