package com.sergeyfitis.moviekeeper.data.models

import kotlinx.serialization.Serializable

@Serializable
class MoviesResponse(
    @Serializable
    val results: List<Movie>)