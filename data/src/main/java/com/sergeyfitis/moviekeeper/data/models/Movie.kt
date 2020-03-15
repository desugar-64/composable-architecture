package com.sergeyfitis.moviekeeper.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    @SerialName("original_title")
    val title: String,
    @SerialName("poster_path")
    var poster: String
)