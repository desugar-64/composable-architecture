package com.sergeyfitis.moviekeeper.feature_movie.reducer

import com.sergeyfitis.moviekeeper.feature_movie.action.MovieAction
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieFeatureAction
import com.sergeyfitis.moviekeeper.feature_movie.action.movieAction
import com.sergeyfitis.moviekeeper.feature_movie.environment.MovieFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieFeatureState
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.sergeyfitis.moviekeeper.feature_movie.state.movieState
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.store.*

internal val movieViewReducer: Reducer<Option<MovieState>, MovieAction, MovieFeatureEnvironment> =
    Reducer { state, action, _ ->
        when (action) {
            is MovieAction.LoadDetails -> reduced(state, noEffects()) // load some additional stuff
            is MovieAction.DetailsLoaded -> TODO()
            is MovieAction.ToggleFavorite -> reduced(
                value = state.map { it.copy(isFavorite = action.isFavorite) },
                effects = noEffects()
            )
        }
    }

val movieFeatureReducer: Reducer<Option<MovieFeatureState>, MovieFeatureAction, MovieFeatureEnvironment> =
    Reducer.combine(
        movieViewReducer.pullback(
            value = MovieFeatureState.movieState,
            action = MovieFeatureAction.movieAction,
            environment = ::identity
        )
    )