package com.sergeyfitis.moviekeeper.navigation.movielist

import androidx.navigation.NavController
import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.MoviesFragmentDirections.Companion.actionMoviesFragmentToMovieDetailsFragment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation

class AppMovieListNavigator(
    private val navigation: NavController
) : MovieListNavigation {

    override fun openMovieDetails(movie: Movie) {
        val action = actionMoviesFragmentToMovieDetailsFragment(movie.id)
        navigation.navigate(action)
    }
}