package com.sergeyfitis.moviekeeper.statemanagement.action

sealed class AppAction

sealed class MoviesAction : AppAction() {
    object Load : MoviesAction()
    data class Loaded(val movies: List<String>) : MoviesAction()

    data class SaveFavorite(val movie: String) : MoviesAction()
    data class RemoveFavorite(val movie: String) : MoviesAction()
}