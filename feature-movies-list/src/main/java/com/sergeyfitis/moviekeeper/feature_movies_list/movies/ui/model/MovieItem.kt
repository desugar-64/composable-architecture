package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class MovieItem(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: Float,
    val voted: Int,
    val isFavorite: Boolean
)