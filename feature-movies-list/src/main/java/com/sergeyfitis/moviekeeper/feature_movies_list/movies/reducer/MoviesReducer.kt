package com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer

import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.moviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.effects.loadMoviesEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.moviesState
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.prelude.types.emptyList
import com.syaremych.composable_architecture.prelude.types.toOption
import com.syaremych.composable_architecture.store.Reducer
import com.syaremych.composable_architecture.store.noEffects
import com.syaremych.composable_architecture.store.pullback
import com.syaremych.composable_architecture.store.reduced
import kotlinx.coroutines.Dispatchers

internal val moviesViewReducer =
    Reducer<MoviesState, MoviesAction, MoviesFeatureEnvironment> { state, action, environment ->
        when (action) {
            is MoviesAction.MovieTapped -> reduced(
                value = state.copy(
                    selectedMovie =
                    state.movies.first { action.movieId == it.id }.toOption()
                ),
                effects = noEffects()
            ) // navigation to the screen details
            MoviesAction.LoadMovies -> reduced(
                value = state,
                effects = listOf(loadMoviesEffect(environment).receiveOn(Dispatchers.Main))
            )
            is MoviesAction.MoviesLoaded -> reduced(
                value = state.copy(
                    movies = action.result.fold(
                        ifLeft = ::emptyList,
                        ifRight = ::identity
                    )
                ),
                effects = noEffects()
            )
        }
    }

// Assemble a big feature reducer from its tiny reducers that describes the feature
val moviesFeatureReducer: Reducer<MoviesFeatureState, MoviesFeatureAction, MoviesFeatureEnvironment> =
    moviesViewReducer.pullback(
        value = MoviesFeatureState.moviesState,
        action = MoviesFeatureAction.moviesAction,
        environment = ::identity
    )
