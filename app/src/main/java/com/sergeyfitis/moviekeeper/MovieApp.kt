package com.sergeyfitis.moviekeeper

import android.app.Application
import com.sergeyfitis.moviekeeper.base.AppFragmentFactory
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.movieViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.moviesViewState
import com.sergeyfitis.moviekeeper.statemanagement.reducer.appReducer
import com.syaremych.composable_architecture.store.Store

class MovieApp : Application() {

    companion object {
        lateinit var appFragmentFactory: AppFragmentFactory
            private set
    }

    private val appStore = Store(
        initialState = AppState.initial(),
        reducer = appReducer
    )

    private val movieStoreLazy by lazy(LazyThreadSafetyMode.NONE) { // TODO: replace with the memoization function or just drop it on the shoulders of a DI container which can take care of lazy injection
        appStore.view(
            toLocalValue = AppState::movieViewState,
            toGlobalAction = AppAction.movieViewActionPrism::reverseGet
        )
    }

    override fun onCreate() {
        super.onCreate()
        appFragmentFactory = AppFragmentFactory(
            appStore.view(
                toLocalValue = AppState::moviesViewState,
                toGlobalAction = AppAction.moviesViewActionPrism::reverseGet
            ),
            ::movieStoreLazy
        )
    }
}