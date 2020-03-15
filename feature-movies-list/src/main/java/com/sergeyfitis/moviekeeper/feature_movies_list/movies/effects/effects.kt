package com.sergeyfitis.moviekeeper.feature_movies_list.movies.effects

import com.sergeyfitis.moviekeeper.data.effects.dataTask
import com.sergeyfitis.moviekeeper.data.effects.getPopularMovies
import com.sergeyfitis.moviekeeper.data.effects.httpClientInstance
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.models.MoviesResponse
import com.syaremych.composable_architecture.prelude.types.rmap
import com.syaremych.composable_architecture.store.Effect
import com.syaremych.composable_architecture.store.map
import com.syaremych.composable_architecture.store.receiveOn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

fun CoroutineScope.loadMoviesEffect(): Effect<MoviesAction> {
    return httpClientInstance()
        .dataTask<MoviesResponse>(this, getPopularMovies)
        .map { MoviesAction.Loaded(it.rmap(MoviesResponse::results)) }
        .receiveOn(Dispatchers.Main.asExecutor())
}