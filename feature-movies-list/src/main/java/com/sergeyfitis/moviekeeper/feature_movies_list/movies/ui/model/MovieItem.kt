package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model

data class MovieItem(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val rating: Float,
    val voted: Int,
    val isFavorite: Boolean
)