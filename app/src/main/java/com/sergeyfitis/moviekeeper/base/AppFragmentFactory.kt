package com.sergeyfitis.moviekeeper.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.sergeyfitis.moviekeeper.statemanagement.action.MovieViewAction
import com.sergeyfitis.moviekeeper.statemanagement.action.MoviesViewAction
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MovieViewState
import com.sergeyfitis.moviekeeper.statemanagement.appstate.MoviesViewState
import com.sergeyfitis.moviekeeper.ui.details.MovieDetailsFragment
import com.sergeyfitis.moviekeeper.ui.favorite.MoviesFavoriteFragment
import com.sergeyfitis.moviekeeper.ui.movies.MoviesFragment
import com.syaremych.composable_architecture.store.Store

class AppFragmentFactory(
    private val moviesStore: () -> Store<MoviesViewState, MoviesViewAction>,
    private val movieStoreLazy: () -> Store<MovieViewState, MovieViewAction>
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            canonicalNameOf<MovieNavHostFragment>() -> MovieNavHostFragment(this)
            canonicalNameOf<MoviesFragment>() -> MoviesFragment(moviesStore.invoke())
            canonicalNameOf<MovieDetailsFragment>() -> MovieDetailsFragment(movieStoreLazy.invoke())
            canonicalNameOf<MoviesFavoriteFragment>() -> MoviesFavoriteFragment()
            else -> super.instantiate(classLoader, className)
        }
    }

    private inline fun <reified T : Fragment> canonicalNameOf(): String? =
        T::class.java.canonicalName
}