package com.sergeyfitis.moviekeeper.feature_movies_favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.actions.FavoriteFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.favoriteState
import com.syaremych.composable_architecture.prelude.identity
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view

class MoviesFavoriteFragment(
    private val store: Store<FavoriteFeatureState, FavoriteFeatureAction>
) : Fragment() {

    private val viewStore: ViewStore<FavoriteState, FavoriteFeatureAction> =
        store.scope(
            toLocalValue = FavoriteFeatureState.favoriteState::get,
            toGlobalAction = ::identity
        ).view

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            val state by viewStore.collectAsState(initial = viewStore.value)
            LazyColumnFor(items = state.favoriteMovies) { movie ->

            }
        }
    }

    override fun onDestroy() {
        store.release()
        super.onDestroy()
    }
}