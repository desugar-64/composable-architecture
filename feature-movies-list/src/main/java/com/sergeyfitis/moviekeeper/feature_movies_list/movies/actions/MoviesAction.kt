package com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions

import com.sergeyfitis.moviekeeper.data.models.Movie

internal sealed class MoviesAction {

    data class MovieTapped(val movie: Movie) : MoviesAction()

/*
    internal data class Movies(val action: MoviesFeatureAction): ViewAction()

    companion object {
        fun loadMovies(scope: CoroutineScope): ViewAction {
            return Movies(MoviesFeatureAction.Load(scope))
        }
        fun loadMovie(movie: Movie): ViewAction {
            return Movies(MoviesFeatureAction.Open(movie))
        }
    }
*/
}