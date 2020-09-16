package com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.actions

sealed class FavoriteFeatureAction {
    data class ToggleFavorite(val movieId: Int, val isFavorite: Boolean) : FavoriteFeatureAction()
}
