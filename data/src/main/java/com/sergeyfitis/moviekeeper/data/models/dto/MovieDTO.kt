package com.sergeyfitis.moviekeeper.data.models.dto

import com.sergeyfitis.moviekeeper.data.models.Category

data class MovieDTO(
    val id: Int,
    val title: String,
    var poster: String,
    var backdrop: String,
    val voteCount: Int,
    val voteAverage: Float,
    val category: Category,
    val genres: List<Int>
)

fun MovieDTO.completeBackdropUrl(): String {
    return "https://image.tmdb.org/t/p/w300/$backdrop"
}

fun MovieDTO.completePosterUrl(): String {
    return "https://image.tmdb.org/t/p/w342/$poster"
}