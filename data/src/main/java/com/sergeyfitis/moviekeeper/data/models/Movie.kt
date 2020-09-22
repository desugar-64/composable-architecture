package com.sergeyfitis.moviekeeper.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    @SerialName("original_title")
    val title: String,
    @SerialName("poster_path")
    var poster: String,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("vote_average")
    val voteAverage: Float
)

fun Movie.completePosterUrl(): String {
    return "https://image.tmdb.org/t/p/w185/$poster"
}