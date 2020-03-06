package com.sergeyfitis.moviekeeper

import android.app.Application
import com.sergeyfitis.moviekeeper.base.MovieFragmentFactory
import com.sergeyfitis.moviekeeper.prelude.types.Option
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MoviesState
import com.sergeyfitis.moviekeeper.statemanagement.reducer.appReducer
import com.sergeyfitis.moviekeeper.statemanagement.store.Store

class MovieApp : Application() {

    companion object {
        lateinit var appFragmentFactory: MovieFragmentFactory
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appFragmentFactory = MovieFragmentFactory(
            Store(
                AppState(
                    MoviesState(
                        Option.empty(),
                        emptyList()
                    ),
                    emptySet(),
                    Option.empty()
                ), appReducer
            )
        )
    }
}