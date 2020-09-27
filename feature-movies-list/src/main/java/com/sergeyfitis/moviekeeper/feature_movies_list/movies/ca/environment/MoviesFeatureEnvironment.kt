package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.environment

import com.sergeyfitis.moviekeeper.data.models.MoviesResponse

class MoviesFeatureEnvironment(
    val nowPlayingMovies: suspend () -> MoviesResponse,
    val upcomingMovies: suspend () -> MoviesResponse,
    val topRatedMovies: suspend () -> MoviesResponse,
)