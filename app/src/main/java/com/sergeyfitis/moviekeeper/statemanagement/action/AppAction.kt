package com.sergeyfitis.moviekeeper.statemanagement.action

import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.prelude.id
import com.sergeyfitis.moviekeeper.prelude.types.Either
import com.sergeyfitis.moviekeeper.prelude.types.Option
import com.sergeyfitis.moviekeeper.prelude.types.Prism
import com.sergeyfitis.moviekeeper.prelude.types.toOption
import kotlinx.coroutines.CoroutineScope

sealed class AppAction {
    sealed class MoviesAction : AppAction() {
        data class Load(val scope: CoroutineScope) : MoviesAction()
        data class Loaded(val movies: Either<Throwable, List<Movie>>) : MoviesAction()
    }

    sealed class MovieAction : AppAction() {
        data class GetDetails(val scope: CoroutineScope, val movieId: Int) : MovieAction()
        data class Loaded(val movie: Movie, val isFavorite: Boolean) : MovieAction()
    }

    companion object {

        val moviesPrism = Prism<AppAction, MoviesAction>(
            get = { appAction ->
                if (appAction is MoviesAction)
                    appAction.toOption()
                else
                    Option.empty()
            },
            reverseGet = ::id
        )
    }
}

fun <T : AppAction> T.asAppAction() = this as AppAction
inline fun <reified T : AppAction> AppAction.asLocalAction(): T? = if (this is T) this else null


sealed class FavoriteAction : AppAction() {
    data class SaveFavorite(val movie: Movie) : FavoriteAction()
    data class RemoveFavorite(val movie: Movie) : FavoriteAction()
}

