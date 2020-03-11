package com.sergeyfitis.moviekeeper.statemanagement.action

import com.sergeyfitis.moviekeeper.models.Movie
import com.syaremych.composable_architecture.prelude.types.Prism
import com.syaremych.composable_architecture.prelude.types.toOption
import kotlinx.coroutines.CoroutineScope

sealed class MoviesViewAction {
    internal data class Movies(val action: MoviesAction): MoviesViewAction()

    companion object {
        fun loadMovies(scope: CoroutineScope): MoviesViewAction {
            return Movies(MoviesAction.Load(scope))
        }
        fun openMovie(movie: Movie): MoviesViewAction {
            return Movies(MoviesAction.Open(movie))
        }

        val moviesActionPrism = Prism<MoviesViewAction, MoviesAction>(
            get = { viewAction ->
                when(viewAction) {
                    is Movies -> viewAction.action.toOption()
                }
            },
            reverseGet = ::Movies
        )
    }
}