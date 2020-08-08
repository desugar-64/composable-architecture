package com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.syaremych.composable_architecture.prelude.types.Prism
import com.syaremych.composable_architecture.prelude.types.toOption
import kotlinx.coroutines.CoroutineScope

internal sealed class ViewAction {

    data class MovieTapped(val movie: Movie) : ViewAction()

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