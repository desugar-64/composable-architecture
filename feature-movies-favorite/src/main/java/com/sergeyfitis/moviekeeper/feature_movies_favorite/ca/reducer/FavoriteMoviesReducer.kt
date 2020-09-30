package com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.reducer

import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.FavoriteFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.FavoriteMoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.favoriteMoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.favoriteMoviesState
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.store.*

internal val favoriteMoviesReducer: Reducer<FavoriteState, FavoriteMoviesAction, Unit> =
    Reducer { state, action, _ ->
        when (action) {
            FavoriteMoviesAction.Load -> reduced(state, Effect.none()) // TODO: load favorite movies
            is FavoriteMoviesAction.ToggleFavorite -> {
                val newState = state.copy(favoriteMovies =
                state.favoriteMovies.toMutableSet().apply {
                    if (action.isFavorite) {
                        add(action.movieId)
                    } else {
                        remove(action.movieId)
                    }
                }
                )
                reduced(newState, Effect.none())
            }
        }
    }

val favoriteMoviesFeatureReducer: Reducer<FavoriteFeatureState, FavoriteFeatureAction, Unit> =
    Reducer.combine(
        favoriteMoviesReducer.pullback(
            value = FavoriteFeatureState.favoriteMoviesState,
            action = FavoriteFeatureAction.favoriteMoviesAction,
            environment = ::identity
        )
    )