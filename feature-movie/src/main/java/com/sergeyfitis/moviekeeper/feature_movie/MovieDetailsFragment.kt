package com.sergeyfitis.moviekeeper.feature_movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieAction
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieFeatureAction
import com.sergeyfitis.moviekeeper.feature_movie.action.movieAction
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieFeatureState
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.sergeyfitis.moviekeeper.feature_movie.state.movieState
import com.sergeyfitis.moviekeeper.feature_movie.ui.MovieRootView
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsFragment(
    private val featureStore: Store<Option<MovieFeatureState>, MovieFeatureAction>
) : Fragment(R.layout.fragment_movie_details) {

    private val viewStore: ViewStore<Option<MovieState>, MovieAction> = featureStore.scope(
        toLocalValue = MovieFeatureState.movieState::get,
        toGlobalAction = MovieFeatureAction.movieAction::reverseGet
    ).view

    private val args by navArgs<MovieDetailsFragmentArgs>()

    init {
        lifecycleScope.launchWhenStarted {
            val movieId = args.movieId
            viewStore.send(MovieAction.LoadDetails(this, movieId))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            MaterialTheme {
                MovieRootView(viewStore = viewStore)
            }
        }
    }

    override fun onDestroy() {
        featureStore.release()
        super.onDestroy()
    }
}
