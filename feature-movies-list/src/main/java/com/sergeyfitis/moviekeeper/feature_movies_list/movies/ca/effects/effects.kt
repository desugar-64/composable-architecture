package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.effects

import com.sergeyfitis.moviekeeper.data.models.MoviesResponse
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.environment.MoviesFeatureEnvironment
import com.syaremych.composable_architecture.prelude.types.Either
import com.syaremych.composable_architecture.prelude.types.recover
import com.syaremych.composable_architecture.prelude.types.rmap
import com.syaremych.composable_architecture.store.Effect
import com.syaremych.composable_architecture.store.eraseToEffect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun loadMoviesEffect(environment: MoviesFeatureEnvironment): Effect<MoviesAction> {
    return flow<Either<Throwable, MoviesResponse>> { emit(Either.recover { environment.movies() }) }
        .map { result -> result.rmap(MoviesResponse::results) }
        .map { result -> MoviesAction.MoviesLoaded(result) }
        .eraseToEffect()
}