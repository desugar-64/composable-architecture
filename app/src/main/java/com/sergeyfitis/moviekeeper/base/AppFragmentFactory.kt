package com.sergeyfitis.moviekeeper.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.sergeyfitis.moviekeeper.feature_movie.MovieDetailsFragment
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieAction
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieFeatureAction
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.MoviesFragment
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.navigation.AppNavigator
import com.sergeyfitis.moviekeeper.ui.favorite.MoviesFavoriteFragment
import com.syaremych.composable_architecture.store.Store

class AppFragmentFactory(
    private val appNavigatorLazy: () -> AppNavigator,
    private val moviesStoreLazy:  () -> Store<MoviesFeatureState, MoviesFeatureAction>,
    private val movieStoreLazy:   () -> Store<MovieFeatureState, MovieFeatureAction>
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            canonicalNameOf<AppNavHostFragment>() -> AppNavHostFragment(this)
            canonicalNameOf<MoviesFragment>() -> MoviesFragment(moviesStoreLazy.invoke(), appNavigatorLazy.invoke())
            canonicalNameOf<MovieDetailsFragment>() -> MovieDetailsFragment(movieStoreLazy.invoke())
            canonicalNameOf<MoviesFavoriteFragment>() -> MoviesFavoriteFragment()
            else -> super.instantiate(classLoader, className)
        }
    }

    private inline fun <reified T : Fragment> canonicalNameOf(): String? =
        T::class.java.canonicalName
}