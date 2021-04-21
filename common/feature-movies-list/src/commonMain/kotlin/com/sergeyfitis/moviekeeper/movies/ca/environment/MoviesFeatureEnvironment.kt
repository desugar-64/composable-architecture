package com.sergeyfitis.moviekeeper.movies.ca.environment

import com.sergeyfitis.moviekeeper.data.models.MoviesResponse

class MoviesFeatureEnvironment(
    val nowPlayingMovies: suspend () -> MoviesResponse,
    val upcomingMovies: suspend () -> MoviesResponse,
    val topRatedMovies: suspend () -> MoviesResponse,
)