package com.sergeyfitis.moviekeeper.statemanagement.action

sealed class AppAction

sealed class MoviesAction : AppAction() {
    object Load : MoviesAction()
    data class Loaded(val movies: List<String>) : MoviesAction()
}

sealed class FavoriteAction : AppAction() {
    data class SaveFavorite(val movie: String) : FavoriteAction()
    data class RemoveFavorite(val movie: String) : FavoriteAction()
}

sealed class MovieDetailsAction : AppAction() {
    data class MovieDetails(val movie: String) : MovieDetailsAction()
}