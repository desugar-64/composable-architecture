package com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation

import com.sergeyfitis.moviekeeper.data.models.Movie

interface MovieListNavigation {
    fun openMovieDetails(movie: Movie)
}