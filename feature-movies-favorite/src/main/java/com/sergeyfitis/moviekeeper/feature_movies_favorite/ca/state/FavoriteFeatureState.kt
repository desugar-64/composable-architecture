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
    val favoriteMovies: List<Movie>
)

internal val FavoriteFeatureState.Companion.favoriteState: Lens<FavoriteFeatureState, FavoriteState>
    get() = Lens(
        get = { featureState ->
            FavoriteState(featureState.favoriteMovies.mapNotNull { movieId ->
                featureState.movies[movieId]
            })
        },
        set = { featureState, state ->
            featureState.copy(favoriteMovies = state.favoriteMovies.map { it.id }.toSet())
        }
    )