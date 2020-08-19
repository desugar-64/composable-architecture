package com.sergeyfitis.moviekeeper.statemanagement.reducer

import com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer.moviesFeatureReducer
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.environment.AppEnvironment
import com.syaremych.composable_architecture.store.Reducer
import com.syaremych.composable_architecture.store.combine
import com.syaremych.composable_architecture.store.pullback

val appReducer: Reducer<AppState, AppAction, AppEnvironment> = /*combine<AppState, AppAction>(
    pullback<MoviesState, AppState, MoviesAction, AppAction>(
        moviesViewReducer,
        valueGet = AppState::moviesViewState,
        valueSet = { appState, movies ->
            appState.moviesViewState = movies
            appState
        },
        toLocalAction = AppAction.moviesViewActionPrism::get,
        toGlobalAction = { map(AppAction.moviesViewActionPrism::reverseGet) }
    ),
    pullback<MovieViewState, AppState, MovieViewAction, AppAction>(
        movieViewReducer,
        valueGet = AppState::movieViewState,
        valueSet = { appState, movieViewState ->
            appState.movieViewState = movieViewState
            appState
        },
        toLocalAction = AppAction.movieViewActionPrism::get,
        toGlobalAction = { map(AppAction.movieViewActionPrism::reverseGet) }
    )
)*/
Reducer.combine(
    moviesFeatureReducer.pullback(
        value = TODO(),
        action = TODO(),
        environment = TODO()
    )
    /*, movieDetailsFeatureReducer*/
)
