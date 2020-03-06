package com.sergeyfitis.moviekeeper.models

import kotlinx.serialization.Serializable

@Serializable
class MoviesResponse(
    @Serializable
    val results: List<Movie>)