package com.sergeyfitis.moviekeeper

import android.app.Application
import com.sergeyfitis.moviekeeper.base.MovieFragmentFactory
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.reducer.movieAppReducer
import com.sergeyfitis.moviekeeper.statemanagement.store.Store

class MovieApp : Application() {

    companion object {
        lateinit var appFragmentFactory: MovieFragmentFactory
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appFragmentFactory = MovieFragmentFactory(
            Store<AppState, AppAction>(AppState(
                emptyList(),
                emptySet(),
                null
            ), ::movieAppReducer)
        )
    }
}