package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.effects

import com.sergeyfitis.moviekeeper.data.models.Category
import com.sergeyfitis.moviekeeper.data.models.MoviesResponse
import com.sergeyfitis.moviekeeper.data.models.RemoteMovie
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.sergeyfitis.moviekeeper.data.models.toDTO
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.syaremych.composable_architecture.prelude.types.Either
import com.syaremych.composable_architecture.prelude.types.recover
import com.syaremych.composable_architecture.prelude.types.rmap
import com.syaremych.composable_architecture.store.Effect
import com.syaremych.composable_architecture.store.eraseToEffect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private val extractMovieResponse: suspend (value: Either<Throwable, MoviesResponse>) -> Either<Throwable, List<RemoteMovie>> =
    { result -> result.rmap(MoviesResponse::results) }

private val toCategorizedDto: (Category) -> suspend (value: Either<Throwable, List<RemoteMovie>>) -> Either<Throwable, List<MovieDTO>> =
    dtoCategory@{ category ->
        return@dtoCategory { response ->
            response.rmap { movies ->
                movies.map { movie -> movie.toDTO(category) }
            }
        }
    }

fun loadNowPlayingEffect(getMovies: suspend () -> MoviesResponse): Effect<MoviesAction> {
    return flow {
        emit(Either.recover { getMovies.invoke() })
    }
        .map(extractMovieResponse)
        .map(toCategorizedDto(Category.NOW_PLAYING))
        .map(MoviesAction::MoviesLoaded)
        .eraseToEffect()
}

fun loadUpcomingEffect(getMovies: suspend () -> MoviesResponse): Effect<MoviesAction> {
    return flow {
        emit(Either.recover { getMovies.invoke() })
    }
        .map(extractMovieResponse)
        .map(toCategorizedDto(Category.UPCOMING))
        .map(MoviesAction::MoviesLoaded)
        .eraseToEffect()
}

fun loadTopRatedEffect(getMovies: suspend () -> MoviesResponse): Effect<MoviesAction> {
    return flow { emit(Either.recover { getMovies.invoke() }) }
        .map(extractMovieResponse)
        .map(toCategorizedDto(Category.TOP_RATED))
        .map(MoviesAction::MoviesLoaded)
        .eraseToEffect()
}