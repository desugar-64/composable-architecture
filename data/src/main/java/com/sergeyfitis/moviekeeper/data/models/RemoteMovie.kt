package com.sergeyfitis.moviekeeper.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteMovie(
    val id: Int,
    @SerialName("original_title")
    val title: String,
    @SerialName("poster_path")
    var poster: String,
    @SerialName("backdrop_path")
    var backdrop: String?,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("vote_average")
    val voteAverage: Float
)

fun RemoteMovie.toDTO(category: Category) = MovieDTO(
    id = id,
    title = title,
    poster = poster,
    backdrop = backdrop ?: poster,
    voteCount = voteCount,
    voteAverage = voteAverage,
    category = category
)