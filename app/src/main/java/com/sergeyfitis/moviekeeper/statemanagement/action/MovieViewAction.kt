package com.sergeyfitis.moviekeeper.statemanagement.action

import com.sergeyfitis.moviekeeper.prelude.types.Option
import com.sergeyfitis.moviekeeper.prelude.types.Prism
import com.sergeyfitis.moviekeeper.prelude.types.toOption
import kotlinx.coroutines.CoroutineScope

sealed class MovieViewAction {
    data class Movie(val action: AppAction.MovieAction) : MovieViewAction()
    data class Favorite(val action: FavoriteAction) : MovieViewAction()

    companion object {
        fun ofAppAction(action: AppAction): MovieViewAction {
            return when(action) {
                is FavoriteAction -> Favorite(action)
                is AppAction.MovieAction -> Movie(action)
                else -> throw RuntimeException("Unsupported action type: ${action::class.java.simpleName}")
            }
        }
        fun getDetails(scope: CoroutineScope, movieId: Int): MovieViewAction {
            return Movie(AppAction.MovieAction.GetDetails(scope, movieId))
        }

        val moviePrism
            get() = Prism<MovieViewAction, AppAction.MovieAction>(
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

val AppAction.movieViewAction: MovieViewAction
    get() = MovieViewAction.ofAppAction(this)

val MovieViewAction.appAction: AppAction
    get() = when(this) {
        is MovieViewAction.Movie -> this.action
        is MovieViewAction.Favorite -> this.action
    }