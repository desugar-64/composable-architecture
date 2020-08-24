package com.sergeyfitis.moviekeeper.statemanagement.reducer

import com.sergeyfitis.moviekeeper.feature_movie.environment.MovieFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movie.reducer.movieFeatureReducer
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.environment.MoviesFeatureEnvironment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer.moviesFeatureReducer
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.action.movieFeatureAction
import com.sergeyfitis.moviekeeper.statemanagement.action.moviesFeatureAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.movieFeatureState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.moviesFeatureState
import com.sergeyfitis.moviekeeper.statemanagement.environment.AppEnvironment
import com.syaremych.composable_architecture.store.Reducer
import com.syaremych.composable_architecture.store.combine
import com.syaremych.composable_architecture.store.pullback

val appReducer: Reducer<AppState, AppAction, AppEnvironment> =
    Reducer.combine(
        moviesFeatureReducer.pullback(
            value = AppState.moviesFeatureState,
            action = AppAction.moviesFeatureAction,
            environment = { appEnvironment ->
                MoviesFeatureEnvironment(appEnvironment.moviesClient.popular)
            }
        ),
        movieFeatureReducer.pullback(
            value = AppState.movieFeatureState,
            action = AppAction.movieFeatureAction,
            environment = { MovieFeatureEnvironment }
        )
    )
