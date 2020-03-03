package com.sergeyfitis.moviekeeper.statemanagement.action

import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.prelude.absurd
import com.sergeyfitis.moviekeeper.prelude.types.Option
import com.sergeyfitis.moviekeeper.prelude.types.Prism
import com.sergeyfitis.moviekeeper.prelude.types.toOption
import kotlinx.coroutines.CoroutineScope

sealed class MoviesViewAction {
    data class Movies(val action: AppAction.MoviesAction): MoviesViewAction()
    data class OpenMovie(val movie: Movie) : MoviesViewAction()

    companion object {
        fun loadMovies(scope: CoroutineScope): MoviesViewAction {
            return Movies(AppAction.MoviesAction.Load(scope))
        }
        fun openMovie(movie: Movie): MoviesViewAction {
            return OpenMovie(movie)
        }

        fun ofAppAction(action: AppAction): MoviesViewAction {
            return when(action) {
                is AppAction.MoviesAction -> Movies(action)
                else -> throw RuntimeException("Unsupported action type: ${action::class.java.simpleName}")
            }
        }

        val moviesPrism = Prism<MoviesViewAction, AppAction.MoviesAction>(
            get = { viewAction ->
                when(viewAction) {
                    is Movies -> viewAction.action.toOption()
                    is OpenMovie -> Option.empty()
                }
            },
            reverseGet = ::Movies
        )

        val moviesViewActionPrism = Prism<AppAction, MoviesViewAction>(
            get = { appAction -> Option.recover { ofAppAction(appAction) } },
            reverseGet = { viewAction ->
                when(viewAction) {
                    is Movies -> viewAction.action
                    is OpenMovie -> absurd(viewAction) // FIXME: conflicting situation
                }
            }
        )
    }
}