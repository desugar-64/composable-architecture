package com.sergeyfitis.moviekeeper.data.models

import kotlinx.serialization.Serializable

@Serializable
class MoviesResponse(
    val results: List<RemoteMovie>
)