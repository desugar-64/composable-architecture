package com.sergeyfitis.moviekeeper.feature_movies_favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.FavoriteFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.FavoriteMoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.favoriteMoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.favoriteMoviesState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ui.FavoriteMoviesRootView
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view

class MoviesFavoriteFragment(
    private val store: Store<FavoriteFeatureState, FavoriteFeatureAction>
) : Fragment() {

    private val viewStore: ViewStore<FavoriteState, FavoriteMoviesAction> =
        store.scope(
            toLocalValue = FavoriteFeatureState.favoriteMoviesState::get,
            toGlobalAction = FavoriteFeatureAction.favoriteMoviesAction::reverseGet
        ).view

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            FavoriteMoviesRootView(viewStore = viewStore)
        }
    }

    override fun onDestroy() {
        store.release()
        super.onDestroy()
    }

    companion object
}

val MoviesFavoriteFragment.Companion.Factory: (Store<FavoriteFeatureState, FavoriteFeatureAction>) -> FragmentFactory
    get() = {
        object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                if (className == MoviesFavoriteFragment::class.java.canonicalName) {
                    return MoviesFavoriteFragment(store = it)
                }
                return super.instantiate(classLoader, className)
            }
        }
    }