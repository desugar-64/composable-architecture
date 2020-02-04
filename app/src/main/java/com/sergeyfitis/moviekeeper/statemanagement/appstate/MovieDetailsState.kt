package com.sergeyfitis.moviekeeper.statemanagement.appstate

import com.sergeyfitis.moviekeeper.models.Movie

data class MovieDetailsState(
    var movie: Movie,
    val isFavorite: Boolean
) {
    companion object {
//        fun initial() = MovieDetailsState(
//            movie = Movie(),
//            isFavorite = false
//        )
    }
}