package com.sergeyfitis.moviekeeper.statemanagement.reducer

import com.sergeyfitis.moviekeeper.feature_movie.action.MovieViewAction
import com.sergeyfitis.moviekeeper.feature_movie.movieViewReducer
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieViewState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer.moviesViewReducer
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.movieViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.moviesViewState
import com.syaremych.composable_architecture.prelude.combine
import com.syaremych.composable_architecture.prelude.pullback

val appReducer = combine<AppState, AppAction>(
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
)
