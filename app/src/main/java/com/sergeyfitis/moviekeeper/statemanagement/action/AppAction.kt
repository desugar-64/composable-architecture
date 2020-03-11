package com.sergeyfitis.moviekeeper.statemanagement.action

import com.sergeyfitis.moviekeeper.models.Movie
import com.syaremych.composable_architecture.prelude.types.Either
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.Prism
import com.syaremych.composable_architecture.prelude.types.toOption
import kotlinx.coroutines.CoroutineScope

sealed class AppAction {
    data class MoviesView(val viewAction: MoviesViewAction) : AppAction()
    data class MovieView(val viewAction: MovieViewAction) : AppAction()

    companion object {

        val moviesViewActionPrism = Prism<AppAction, MoviesViewAction>(
            get = { appAction ->
                when (appAction) {
                    is MoviesView -> appAction.viewAction.toOption()
                    else -> Option.empty()
                }
            },
            reverseGet = AppAction::MoviesView
        )
        val movieViewActionPrism = Prism<AppAction, MovieViewAction>(
            get = { appAction -> when(appAction) {
                is MovieView -> appAction.viewAction.toOption()
                else -> Option.empty()
            } },
            reverseGet = AppAction::MovieView
        )
    }
}

sealed class MovieAction {
    data class GetDetails(val scope: CoroutineScope, val movieId: Int) : MovieAction()
    data class Loaded(val movie: Movie, val isFavorite: Boolean) : MovieAction()
}

sealed class MoviesAction {
    data class Load(val scope: CoroutineScope) : MoviesAction()
    data class Loaded(val movies: Either<Throwable, List<Movie>>) : MoviesAction()
    data class Open(val movie: Movie) : MoviesAction()
}

sealed class FavoriteAction : AppAction() {
    data class SaveFavorite(val movie: Movie) : FavoriteAction()
    data class RemoveFavorite(val movie: Movie) : FavoriteAction()
}

