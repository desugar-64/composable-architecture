package com.sergeyfitis.moviekeeper.data.effects

import com.sergeyfitis.moviekeeper.data.api.MoviesClient
import com.sergeyfitis.moviekeeper.data.models.MoviesResponse
import com.syaremych.composable_architecture.store.Effect
import com.syaremych.composable_architecture.store.eraseToEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow

//val CoroutineScope.getPopularMovies: suspend (MoviesClient) -> Either<Throwable, MoviesResponse>
//    get() = { client -> Either.recover { client.popular() } }
//    requestBuilder() withA httpUrlLens.lift(urlPath("movie/popular"))

fun CoroutineScope.getPopularMovies(client: MoviesClient): Effect<MoviesResponse> {
    return flow<MoviesResponse> {
        emit(client.popular())
    }.eraseToEffect()
}