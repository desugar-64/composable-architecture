package com.sergeyfitis.moviekeeper.feature_movies_list.movies.effects

import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.environment.MoviesFeatureEnvironment
import com.syaremych.composable_architecture.prelude.types.Either
import com.syaremych.composable_architecture.prelude.types.recover
import com.syaremych.composable_architecture.prelude.types.rmap
import com.syaremych.composable_architecture.store.Effect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/*
fun CoroutineScope.loadMoviesEffect(): Effect<MoviesAction> {
    return httpClientInstance()
        .dataTask<MoviesResponse>(this, getPopularMovies)
        .map { MoviesAction.MoviesLoaded(it.rmap(MoviesResponse::results)) }
        .receiveOn(Dispatchers.Main.asExecutor())
}
*/

fun CoroutineScope.loadMoviesEffect(environment: MoviesFeatureEnvironment): Effect<MoviesAction> {
    return Effect<MoviesAction> { callback ->
        launch(coroutineContext) {
            val result = Either.recover { environment.movies() }
            callback(MoviesAction.MoviesLoaded(result.rmap { it.results }))
        }
    }
}