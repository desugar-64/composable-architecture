package com.sergeyfitis.moviekeeper.statemanagement.reducer

import com.sergeyfitis.moviekeeper.prelude.combine
import com.sergeyfitis.moviekeeper.prelude.id
import com.sergeyfitis.moviekeeper.prelude.pullback
import com.sergeyfitis.moviekeeper.prelude.types.Either
import com.sergeyfitis.moviekeeper.prelude.types.ofNullable
import com.sergeyfitis.moviekeeper.prelude.types.right
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.action.asLocalAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieState
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
    pullback(
        moviesReducer,
        valueGet = AppState::movies,
        valueSet = { appState, movies ->
            appState.movies = movies
            appState
        },
        toLocalAction = { asLocalAction() },
        toGlobalAction = { this?.asAppAction() }
    ),
    pullback(
        movieViewReducer,
        valueGet = {
            it.movieState.fold(
                ifLeft = { MovieState(Either.ofNullable(null), false) },
                ifRight = ::id
            )
        },
        valueSet = { appState, movieDetailsState ->
            appState.movieState = movieDetailsState.right()
            appState
        },
        toLocalAction = { asLocalAction() },
        toGlobalAction = { this?.asAppAction() }
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