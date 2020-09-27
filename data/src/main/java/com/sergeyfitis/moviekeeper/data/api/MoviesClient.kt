package com.sergeyfitis.moviekeeper.data.api

import com.sergeyfitis.moviekeeper.data.models.MoviesResponse

class MoviesClient(
    val nowPlaying: suspend () -> MoviesResponse,
    val upcoming: suspend () -> MoviesResponse,
    val topRated: suspend () -> MoviesResponse
) {
    companion object
}

val MoviesClient.Companion.live: MoviesClient
    get() = MoviesClient(
        nowPlaying = moviesApi::nowPlaying,
        upcoming = moviesApi::upcoming,
        topRated = moviesApi::topRated
    )
