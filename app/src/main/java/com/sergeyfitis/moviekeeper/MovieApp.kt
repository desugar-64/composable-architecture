package com.sergeyfitis.moviekeeper

import android.app.Application
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sergeyfitis.moviekeeper.base.AppActivityLifecycleCallbacks
import com.sergeyfitis.moviekeeper.base.AppFragmentFactory
import com.sergeyfitis.moviekeeper.navigation.AppNavigator
import com.sergeyfitis.moviekeeper.navigation.movie.AppMovieNavigator
import com.sergeyfitis.moviekeeper.navigation.movielist.AppMovieListNavigator
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.movieViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.moviesViewState
import com.sergeyfitis.moviekeeper.statemanagement.reducer.appReducer
import com.sergeyfitis.moviekeeper.ui.MainActivity
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.getOrThrow
import com.syaremych.composable_architecture.prelude.types.toOption
import com.syaremych.composable_architecture.store.Store
import kotlinx.android.synthetic.main.activity_main.*

class MovieApp : Application() {

    companion object {
        lateinit var appFragmentFactory: AppFragmentFactory
            private set
    }

    private val appStore = Store(
        initialState = AppState.initial(),
        reducer = appReducer
    )

    private var mainNavHostActivity: Option<MainActivity> =
        Option.empty() // Is this good enough solution?

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(AppActivityLifecycleCallbacks(
            onMainActivityCreated = { mainNavHostActivity = it.toOption() },
            onMainActivityDestroyed = { mainNavHostActivity = Option.empty() }
        ))

        val navControllerLazy: () -> NavController = {
            mainNavHostActivity
                .getOrThrow()
                .main_nav_host
                .findNavController()
        }

        val appNavigatorLazy = {
            AppNavigator(
                movieListNavigation = AppMovieListNavigator(navControllerLazy.invoke()),
                movieNavigation = AppMovieNavigator(navControllerLazy.invoke())
            )
        }

        val movieStoreLazy = {
            appStore.view(
                toLocalValue = AppState::movieViewState,
                toGlobalAction = AppAction.movieViewActionPrism::reverseGet
            )
        }
        val moviesStoreLazy = {
            appStore.view(
                toLocalValue = AppState::moviesViewState,
                toGlobalAction = AppAction.moviesViewActionPrism::reverseGet
            )
        }
        appFragmentFactory = AppFragmentFactory(
            appNavigatorLazy,
            moviesStoreLazy,
            movieStoreLazy
        )
    }
}