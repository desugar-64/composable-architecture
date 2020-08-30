package com.sergeyfitis.moviekeeper.feature_movies_list.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.InnerPadding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.sergeyfitis.moviekeeper.feature_movies_list.R
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.moviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.adapter.MoviesAdapter
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.moviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.MovieItem
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesFragment(
    private val featureStore: Store<MoviesFeatureState, MoviesFeatureAction>,
    private val navigator: MovieListNavigation
) : Fragment(R.layout.fragment_movies) {

    private lateinit var rvMovies: RecyclerView

    private val viewStore: ViewStore<MoviesState, MoviesAction> =
        featureStore.scope<MoviesState, MoviesAction>(
            toLocalValue = MoviesFeatureState.moviesState::get,
            toGlobalAction = MoviesFeatureAction.moviesAction::reverseGet
        ).view

    init {
        lifecycleScope.launchWhenCreated { viewStore.send(MoviesAction.LoadMovies) }
    }

    private fun render(viewState: MoviesState) {
        rvMovies.adapter = MoviesAdapter(viewState.movies) { movie ->
            viewStore.send(MoviesAction.MovieTapped(movie))
            navigator.openMovieDetails(movie)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(context = requireContext()).apply {
            this.setContent {
                viewStore.collectAsState(viewStore.value).value.let { state ->
                    MaterialTheme {
                        LazyColumnFor(
                            state.movies,
                            contentPadding = InnerPadding(bottom = 16.dp)
                        ) { item ->
                            MovieItem(movie = item)
                        }
                    }
                }

            }
        }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        rvMovies = view.findViewById(R.id.rv_movies)
//        rvMovies.layoutManager = LinearLayoutManager(requireContext())
//        viewStore
//            .onEach { state -> render(state) }
//            .onCompletion { viewStore.dispose() }
//            .launchIn(viewLifecycleOwner.lifecycleScope)
//    }

    override fun onDestroy() {
        featureStore.release()
        super.onDestroy()
    }

}