package com.sergeyfitis.moviekeeper.navigation

import com.sergeyfitis.moviekeeper.feature_movie.navigation.MovieNavigation
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation

class AppNavigator(
    movieListNavigation: MovieListNavigation,
    movieNavigation: MovieNavigation
) :
    MovieListNavigation by movieListNavigation,
    MovieNavigation by movieNavigation {

}