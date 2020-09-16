package com.sergeyfitis.moviekeeper.feature_movies_list.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.Action
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.MoviesRoot
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.State
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.init
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesFragment(
    private val featureStore: Store<MoviesFeatureState, MoviesFeatureAction>,
    private val navigator: MovieListNavigation
) : Fragment() {

    private val viewStore: ViewStore<State, Action> =
        featureStore.scope(
            toLocalValue = State::init,
            toGlobalAction = MoviesFeatureAction::init
        ).view

    init {
        lifecycleScope.launchWhenCreated { viewStore.send(Action.LoadList) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(context = requireContext()).apply {
        setContent {
            MoviesRoot(viewStore = viewStore, navigator = navigator)
        }
    }

    override fun onDestroy() {
        featureStore.release()
        super.onDestroy()
    }
}

