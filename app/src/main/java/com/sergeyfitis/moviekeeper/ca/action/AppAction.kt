package com.sergeyfitis.moviekeeper.ca.action

import com.sergeyfitis.moviekeeper.data.models.dto.GenreDTO
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.FavoriteFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.prelude.types.Either
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.Prism
import com.syaremych.composable_architecture.prelude.types.toOption

sealed class AppAction {
    data class Movies(val action: MoviesFeatureAction) : AppAction()
    data class Movie(val action: MovieFeatureAction) : AppAction()
    data class Favorite(val action: FavoriteFeatureAction) : AppAction()

    sealed class Genres : AppAction() {
        object Load : Genres()
        data class Loaded(val result: Either<Throwable, List<GenreDTO>>) : Genres()
    }

    companion object
}

val AppAction.Companion.moviesFeatureAction
    get() = Prism<AppAction, MoviesFeatureAction>(
        get = { appAction ->
            if (appAction is AppAction.Movies)
                appAction.action.toOption()
            else
                Option.empty()
        },
        reverseGet = AppAction::Movies
    )

val AppAction.Companion.movieFeatureAction
    get() = Prism<AppAction, MovieFeatureAction>(
        get = { appAction ->
            if (appAction is AppAction.Movie)
                appAction.action.toOption()
            else
                Option.empty()
        },
        reverseGet = AppAction::Movie
    )

val AppAction.Companion.favoriteFeatureAction
    get() = Prism<AppAction, FavoriteFeatureAction>(
        get = { appAction ->
            if (appAction is AppAction.Favorite)
                appAction.action.toOption()
            else
                Option.empty()
        },
        reverseGet = AppAction::Favorite
    )

val AppAction.Companion.genresAction
    get() = Prism<AppAction, AppAction.Genres>(
        get = { appAction ->
            if (appAction is AppAction.Genres)
                appAction.toOption()
            else
                Option.empty()
        },
        reverseGet = ::identity
    )
