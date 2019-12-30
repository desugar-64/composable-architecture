package com.sergeyfitis.moviekeeper.statemanagement.reducer

import com.sergeyfitis.moviekeeper.prelude.combine
import com.sergeyfitis.moviekeeper.prelude.pullback
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieDetailsAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MoviesAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.ui.details.movieDetailsReducer
import com.sergeyfitis.moviekeeper.ui.movies.moviesReducer


/*fun appReducer(appState: AppState, appAction: AppAction) {
    when (appAction) {
        is AppAction.Movies -> TODO()
        is AppAction.Favorites -> TODO()
        is AppAction.MovieDetails -> TODO()
    }
}*/

val appReducer = combine<AppState, AppAction>(
    pullback(
        moviesReducer,
        valueGet = AppState::movies,
        valueSet = { appState, movies -> appState.movies = movies },
        toLocalAction = { it as? MoviesAction },
        toGlobalAction = { it as? AppAction }
    ),
     pullback(
         movieDetailsReducer,
         valueGet = AppState::movieDetailsState,
         valueSet = { appState, movieDetailsState -> appState.movieDetailsState = movieDetailsState },
         toLocalAction = { it as? MovieDetailsAction },
         toGlobalAction = { it as? AppAction }
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