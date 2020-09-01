package com.sergeyfitis.moviekeeper.feature_movies_list.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.InnerPadding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction.*
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction.Movies
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.MovieViewItem
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model.MovieItem
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesFragment(
    private val featureStore: Store<MoviesFeatureState, MoviesFeatureAction>,
    private val navigator: MovieListNavigation
) : Fragment() {

    // Local domain specific state and action
    internal data class State(val movies: List<MovieItem>) {
        companion object
    }

    internal sealed class Action {
        object LoadList : Action()
        data class MovieTapped(val movieId: Int) : Action()
        data class FavoriteToggle(val movieId: Int, val isFavorite: Boolean) : Action()
    }

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
            MaterialTheme {
                val stateHolder = viewStore.collectAsState(viewStore.value)
                LazyColumnFor(
                    stateHolder.value.movies,
                    contentPadding = InnerPadding(bottom = 16.dp),
                ) { item ->
                    MovieViewItem(
                        item = item,
                        onClick = {
                            viewStore.send(Action.MovieTapped(item.id))
                            navigator.openMovieDetails(item.id)
                        },
                        toggleFavorite = { isFavorite ->
                            viewStore.send(Action.FavoriteToggle(item.id, isFavorite))
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        featureStore.release()
        super.onDestroy()
    }
}

private fun MoviesFragment.State.Companion.init(featureState: MoviesFeatureState): MoviesFragment.State {
    return MoviesFragment.State(
        movies = featureState.movies.map { movie ->
            MovieItem(
                id = movie.id,
                title = movie.title,
                posterUrl = "https://image.tmdb.org/t/p/w500/${movie.poster}",
                rating = movie.voteAverage,
                voted = movie.voteCount,
                isFavorite = featureState.favorites.any { movie.id == it }
            )
        }
    )
}

private fun MoviesFeatureAction.Companion.init(action: MoviesFragment.Action): MoviesFeatureAction {
    return when (action) {
        MoviesFragment.Action.LoadList -> Movies(LoadMovies)
        is MoviesFragment.Action.MovieTapped -> Movies(MovieTapped(action.movieId))
        is MoviesFragment.Action.FavoriteToggle -> Movies(
            ToggleFavorite(
                action.movieId,
                action.isFavorite
            )
        )
    }
}
