package com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer

import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.moviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.effects.loadMoviesEffect
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.moviesState
import com.syaremych.composable_architecture.prelude.id
import com.syaremych.composable_architecture.store.Reducer
import com.syaremych.composable_architecture.store.noEffects
import com.syaremych.composable_architecture.store.pullback
import com.syaremych.composable_architecture.store.reduced

internal val moviesViewReducer =
    Reducer<MoviesState, MoviesAction, MoviesFeatureEnvironment> { state, action, environment ->
        when (action) {
            is MoviesAction.MovieTapped -> TODO() // navigation to the screen details
            is MoviesAction.LoadMovies -> reduced(
                value = state,
                effects = listOf(action.scope.loadMoviesEffect(environment))
            )
            is MoviesAction.MoviesLoaded -> reduced(
                value = state.copy(
                    movies = action
                        .result
                        .fold(
                            ifLeft = { emptyList() },
                            ifRight = ::id
                        )
                ),
                effects = noEffects()
            )
        }
    }

// Assemble big feature reducer from its tiny reducers that describes the feature
val moviesFeatureReducer: Reducer<MoviesFeatureState, MoviesFeatureAction, MoviesFeatureEnvironment> =
    moviesViewReducer.pullback(
        MoviesFeatureState.moviesState,
        MoviesFeatureAction.moviesAction,
        ::id
    )
