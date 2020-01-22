package com.sergeyfitis.moviekeeper.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.ArrayListSerializer

@Serializable
class MoviesResponse(
    @Serializable(with = ArrayListSerializer::class)
    val results: List<Movie>)