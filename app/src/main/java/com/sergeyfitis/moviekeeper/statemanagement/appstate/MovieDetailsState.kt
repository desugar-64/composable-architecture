package com.sergeyfitis.moviekeeper.statemanagement.appstate

class MovieDetailsState(
    val movie: String,
    val isFavorite: Boolean
) {
    companion object {
        fun initial() = MovieDetailsState(
            movie = "",
            isFavorite = false
        )
    }
}