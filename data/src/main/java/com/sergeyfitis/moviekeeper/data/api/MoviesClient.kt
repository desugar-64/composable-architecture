package com.sergeyfitis.moviekeeper.data.api

import com.sergeyfitis.moviekeeper.data.models.MoviesResponse

class MoviesClient(
    val popular: suspend () -> MoviesResponse
) {
    companion object
}

val MoviesClient.Companion.live: MoviesClient
    get() = MoviesClient(moviesApi::popular)