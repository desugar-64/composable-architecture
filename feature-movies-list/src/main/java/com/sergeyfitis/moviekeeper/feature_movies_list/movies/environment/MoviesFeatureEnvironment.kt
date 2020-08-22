package com.sergeyfitis.moviekeeper.feature_movies_list.movies.environment

import com.sergeyfitis.moviekeeper.data.models.MoviesResponse

class MoviesFeatureEnvironment(
    val movies: suspend () -> MoviesResponse
)