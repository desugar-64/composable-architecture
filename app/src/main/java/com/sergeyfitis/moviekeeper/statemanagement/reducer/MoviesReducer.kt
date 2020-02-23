package com.sergeyfitis.moviekeeper.statemanagement.reducer

import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.prelude.combine
import com.sergeyfitis.moviekeeper.prelude.pullback
import com.sergeyfitis.moviekeeper.prelude.types.Option
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieViewAction
import com.sergeyfitis.moviekeeper.statemanagement.action.appAction
import com.sergeyfitis.moviekeeper.statemanagement.action.movieViewAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.movieViewState
import com.sergeyfitis.moviekeeper.ui.details.movieViewReducer
import com.sergeyfitis.moviekeeper.ui.movies.moviesReducer


/*fun appReducer(appState: AppState, appAction: AppAction) {
    when (appAction) {
        is AppAction.Movies -> TODO()
        is AppAction.Favorites -> TODO()
        is AppAction.MovieDetails -> TODO()
    }
}*/

val appReducer = combine<AppState, AppAction>(
    pullback<List<Movie>, AppState, AppAction.MoviesAction, AppAction>(
        moviesReducer,
        valueGet = AppState::movies,
        valueSet = { appState, movies ->
            appState.movies = movies
            appState
        },
        toLocalAction = AppAction.moviesPrism::get,
        toGlobalAction = { map(AppAction.moviesPrism::reverseGet) }
    ),
    pullback<MovieViewState, AppState, MovieViewAction, AppAction>(
        movieViewReducer,
        valueGet = AppState::movieViewState,
        valueSet = { appState, movieViewState ->
            appState.movieViewState = movieViewState
            appState
        },
        toLocalAction = { Option.recover(this::movieViewAction) },
        toGlobalAction = { map(MovieViewAction::appAction) }
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