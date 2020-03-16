package com.sergeyfitis.moviekeeper.feature_movie.action

import com.sergeyfitis.moviekeeper.data.models.Movie

sealed class FavoriteAction {
    data class SaveFavorite(val movie: Movie) : FavoriteAction()
    data class RemoveFavorite(val movie: Movie) : FavoriteAction()
}
