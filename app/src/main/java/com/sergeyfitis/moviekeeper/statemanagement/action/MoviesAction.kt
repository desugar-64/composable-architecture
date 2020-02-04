package com.sergeyfitis.moviekeeper.statemanagement.action

import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.prelude.types.Either
import kotlinx.coroutines.CoroutineScope

sealed class AppAction

fun <T : AppAction> T.asAppAction() = this as AppAction
inline fun <reified T : AppAction> AppAction.asLocalAction(): T? = if (this is T) this else null

sealed class MoviesAction : AppAction() {
    data class Load(val scope: CoroutineScope) : MoviesAction()
    data class Loaded(val movies: Either<Throwable, List<Movie>>) : MoviesAction()
}

sealed class FavoriteAction : AppAction() {
    data class SaveFavorite(val movie: String) : FavoriteAction()
    data class RemoveFavorite(val movie: String) : FavoriteAction()
}

sealed class MovieDetailsAction : AppAction() {
    data class GetBy(val scope: CoroutineScope, val movieId: Int) : MovieDetailsAction()
    data class Loaded(val movie: Movie, val isFavorite: Boolean) : MovieDetailsAction()
}