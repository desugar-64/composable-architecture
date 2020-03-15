package com.sergeyfitis.moviekeeper.statemanagement.reducer

import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesViewAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.reducer.moviesViewReducer
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesViewState
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieViewAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.movieViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.moviesViewState
import com.sergeyfitis.moviekeeper.ui.details.movieViewReducer
import com.syaremych.composable_architecture.prelude.combine
import com.syaremych.composable_architecture.prelude.pullback


/*fun appReducer(appState: AppState, appAction: AppAction) {
    when (appAction) {
        is AppAction.Movies -> TODO()
        is AppAction.Favorites -> TODO()
        is AppAction.MovieDetails -> TODO()
    }
}*/

val appReducer = combine<AppState, AppAction>(
    pullback<MoviesViewState, AppState, MoviesViewAction, AppAction>(
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

/*val appReducer = combine<AppState, AppAction>(
    pullback(
        moviesReducer,
        value = ValueHolder::value,
        action = { it as? MoviesAction }
    ),
    pullback(
        movieDetailsReducer,
        value = AppState::movieDetailsState,
        action = { it as? MovieDetailsAction }
    )
)*/