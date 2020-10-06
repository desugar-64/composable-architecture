package com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions

import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.syaremych.composable_architecture.prelude.types.Either

sealed class MoviesAction {
    data class MovieTapped(val movieId: Int) : MoviesAction()
    object LoadMovies : MoviesAction()
    data class MoviesLoaded(val result: Either<Throwable, List<MovieDTO>>) : MoviesAction()
}