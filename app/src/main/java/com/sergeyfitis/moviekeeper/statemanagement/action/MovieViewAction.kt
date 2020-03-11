package com.sergeyfitis.moviekeeper.statemanagement.action

import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.Prism
import com.syaremych.composable_architecture.prelude.types.toOption
import kotlinx.coroutines.CoroutineScope

sealed class MovieViewAction {
    data class Movie(val action: MovieAction) : MovieViewAction()
    data class Favorite(val action: FavoriteAction) : MovieViewAction()

    companion object {
        fun getDetails(scope: CoroutineScope, movieId: Int): MovieViewAction {
            return Movie(MovieAction.GetDetails(scope, movieId))
        }

        val moviePrism
            get() = Prism<MovieViewAction, MovieAction>(
                get = { viewAction ->
                    when(viewAction) {
                        is Movie -> viewAction.action.toOption()
                        is Favorite -> Option.empty()
                    }
                },
                reverseGet = ::Movie
            )

    }
}