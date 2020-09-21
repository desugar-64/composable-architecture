package com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action

sealed class FavoriteMoviesAction {
    object Load : FavoriteMoviesAction()
    data class ToggleFavorite(val movieId: Int, val isFavorite: Boolean) : FavoriteMoviesAction()
}