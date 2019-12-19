package com.sergeyfitis.moviekeeper.statemanagement.reducer

import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MoviesAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState

fun movieAppReducer(state: AppState, action: AppAction) {
    when(action) {
        MoviesAction.Load -> state.movies = getMovies()
        is MoviesAction.Loaded -> state.movies = getMovies()
        is MoviesAction.SaveFavorite -> TODO()
        is MoviesAction.RemoveFavorite -> TODO()
    }
}

private fun getMovies(): List<String> {
    return listOf(
        "Parasite (2019)",
        "The Irishman (2019)",
        "Burning (2018)",
        "Long Day's Journey Into Night (2018)",
        "Birds of Passage (2018)"
    )
}
