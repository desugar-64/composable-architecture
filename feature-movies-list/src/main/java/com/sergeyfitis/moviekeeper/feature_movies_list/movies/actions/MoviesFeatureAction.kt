package com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.syaremych.composable_architecture.prelude.types.Either
import com.syaremych.composable_architecture.prelude.types.Prism
import kotlinx.coroutines.CoroutineScope

sealed class MoviesFeatureAction {
    data class Load(val scope: CoroutineScope) : MoviesFeatureAction()
    data class Loaded(val movies: Either<Throwable, List<Movie>>) : MoviesFeatureAction()
    data class Open(val movie: Movie) : MoviesFeatureAction()

    companion object
}

internal val MoviesFeatureAction.Companion.moviesAction: Prism<MoviesFeatureAction, MoviesAction>
    get() = Prism(
        get = { featureAction -> TODO() },
        reverseGet = { viewAction -> TODO() }
    )