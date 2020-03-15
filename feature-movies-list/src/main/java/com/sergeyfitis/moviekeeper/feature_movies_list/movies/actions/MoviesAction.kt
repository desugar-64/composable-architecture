package com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.syaremych.composable_architecture.prelude.types.Either
import kotlinx.coroutines.CoroutineScope

sealed class MoviesAction {
    data class Load(val scope: CoroutineScope) : MoviesAction()
    data class Loaded(val movies: Either<Throwable, List<Movie>>) : MoviesAction()
    data class Open(val movie: Movie) : MoviesAction()
}