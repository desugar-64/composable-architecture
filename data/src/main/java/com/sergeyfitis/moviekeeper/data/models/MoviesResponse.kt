package com.sergeyfitis.moviekeeper.models

import com.sergeyfitis.moviekeeper.data.models.Movie
import kotlinx.serialization.Serializable

@Serializable
class MoviesResponse(
    @Serializable
    val results: List<Movie>)