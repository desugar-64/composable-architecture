package com.sergeyfitis.moviekeeper.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.sergeyfitis.moviekeeper.statemanagement.action.AppAction
import com.sergeyfitis.moviekeeper.statemanagement.action.asAppAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.AppState
import com.sergeyfitis.moviekeeper.statemanagement.store.Store
import com.sergeyfitis.moviekeeper.ui.details.MovieDetailsFragment
import com.sergeyfitis.moviekeeper.ui.favorite.MoviesFavoriteFragment
import com.sergeyfitis.moviekeeper.ui.movies.MoviesFragment

class MovieFragmentFactory(
    private val store: Store<AppState, AppAction>
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            canonicalNameOf<MovieNavHostFragment>() -> MovieNavHostFragment(this)
            canonicalNameOf<MoviesFragment>() -> MoviesFragment(
                store.view(
                    toLocalValue = AppState::movies,
                    toGlobalAction = { it.asAppAction() })
            )
            canonicalNameOf<MovieDetailsFragment>() -> MovieDetailsFragment(
                store.view(
                    toLocalValue = { appState ->
//                        appState.
                        TODO()
                    },
                    toGlobalAction = { it.asAppAction() })
            )
            canonicalNameOf<MoviesFavoriteFragment>() -> MoviesFavoriteFragment()
            else -> super.instantiate(classLoader, className)
        }
    }

    private inline fun <reified T : Fragment> canonicalNameOf(): String? =
        T::class.java.canonicalName
}