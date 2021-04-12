package com.sergeyfitis.moviekeeper.data.models

import kotlinx.serialization.Serializable

@Serializable
class GenresResponse(
    val genres: List<RemoteGenre>
)