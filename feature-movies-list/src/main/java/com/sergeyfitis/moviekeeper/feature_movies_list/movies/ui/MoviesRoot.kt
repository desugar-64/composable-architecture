package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRow
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model.MovieItem
import com.syaremych.composable_architecture.store.*

@Composable
internal fun MoviesRoot(viewStore: ViewStore<State, Action>, navigator: MovieListNavigation) {
    MaterialTheme {
        Column {

            var activeTab by remember { mutableStateOf(Tab.Recomended) }

            TabRow(selectedTabIndex = activeTab.ordinal, backgroundColor = Color.White) {
                Tab.values().forEach { tab ->
                    MovieTab(
                        tab = tab,
                        isActive = tab == activeTab,
                        onClick = { activeTab = it }
                    )
                }
            }
            val stateHolder = viewStore.collectAsState(viewStore.value)
            LazyColumnFor(
                stateHolder.value.movies,
                contentPadding = PaddingValues(bottom = 16.dp),
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

@Preview(
    device = Devices.NEXUS_5,
    widthDp = 360,
    showBackground = true,
    backgroundColor = 0xffffffL
)
@Composable
private fun RootPreview() {
    val mockStore = Store.init<State, Action, Unit>(
        initialState = State(listOf(MovieItem(0, "Title", "/url", 1f, 10, false))),
        reducer = Reducer { state, _, _ -> reduced(state, noEffects()) },
        environment = Unit
    )
    val navigator = object : MovieListNavigation {
        override fun openMovieDetails(movieId: Int) {}
    }
    MaterialTheme {
        MoviesRoot(mockStore.view, navigator)
    }
}


// Local domain specific state and action
internal data class State(val movies: List<MovieItem>) {
    companion object
}

internal sealed class Action {
    object LoadList : Action()
    data class MovieTapped(val movieId: Int) : Action()
    data class FavoriteToggle(val movieId: Int, val isFavorite: Boolean) : Action()
}

internal fun State.Companion.init(featureState: MoviesFeatureState): State {
    return State(
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

internal fun MoviesFeatureAction.Companion.init(action: Action): MoviesFeatureAction {
    return when (action) {
        Action.LoadList -> MoviesFeatureAction.Movies(MoviesAction.LoadMovies)
        is Action.MovieTapped -> MoviesFeatureAction.Movies(
            MoviesAction.MovieTapped(
                action.movieId
            )
        )
        is Action.FavoriteToggle -> MoviesFeatureAction.Movies(
            MoviesAction.ToggleFavorite(
                action.movieId,
                action.isFavorite
            )
        )
    }
}
