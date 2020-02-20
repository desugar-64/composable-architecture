package com.sergeyfitis.moviekeeper.statemanagement.action

import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.prelude.types.Option
import com.sergeyfitis.moviekeeper.prelude.types.Prism
import com.sergeyfitis.moviekeeper.prelude.types.toOption
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
                getOption = { viewAction ->
                    when(viewAction) {
                        is Movie -> viewAction.action.toOption()
                        is Favorite -> Option.empty()
                    }
                },
                reverseGet = ::Movie
            )
    }
}

sealed class MovieAction {
    data class GetDetails(val scope: CoroutineScope, val movieId: Int) : MovieAction()
    data class Loaded(val movie: Movie, val isFavorite: Boolean) : MovieAction()
}