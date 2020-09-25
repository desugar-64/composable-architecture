package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.data.models.completeBackdropUrl
import com.sergeyfitis.moviekeeper.data.models.completePosterUrl
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model.MovieItem
import com.syaremych.composable_architecture.store.*
import ui.MoviePoster

@Composable
internal fun MoviesRoot(viewStore: ViewStore<State, Action>, navigator: MovieListNavigation) {
    MaterialTheme {
        Column {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Movie Keeper",
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.h4
            )
            ScrollableColumn {
                val state by viewStore.collectAsState(viewStore.value)
                val onClick: (MovieItem) -> () -> Unit = onClick@{ movie ->
                    return@onClick {
                        viewStore.send(Action.MovieTapped(movie.id))
                        navigator.openMovieDetails(movie.id)
                    }
                }
                MoviesRow(
                    rowTitle = "Now Playing"
                ) {
                    MoviesHorizontalList(state.movies) { movieItem ->
                        MoviePosterItem(
                            posterUrl = movieItem.posterUrl,
                            onClick = onClick(movieItem)
                        )
                    }
                }

                MoviesRow(
                    rowTitle = "Upcoming"
                ) {
                    MoviesHorizontalList(state.movies) { movieItem ->
                        MovieBackdropItem(item = movieItem, onClick = onClick(movieItem))
                    }
                }
            }
        }
    }
}

@Composable
private fun MoviesHorizontalList(
    movies: List<MovieItem>,
    viewHolder: @Composable LazyItemScope.(MovieItem) -> Unit
) {
    LazyRowFor(
        items = movies,
        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, bottom = 8.dp),
        itemContent = viewHolder
    )
}

@Composable
private fun MovieBackdropItem(
    modifier: Modifier = Modifier,
    item: MovieItem,
    onClick: () -> Unit
) {
    Column {
        val posterWidth = 250.dp
        MoviePoster(
            modifier = modifier,
            url = item.backdropUrl,
            onClick = onClick,
            aspectRatio = 1.6f,
            posterWidth = posterWidth
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.width(posterWidth),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = Color.DarkGray
        )
    }
    Spacer(modifier = Modifier.width(16.dp))
}

@Composable
private fun MoviePosterItem(
    modifier: Modifier = Modifier,
    posterUrl: String,
    onClick: () -> Unit
) {
    MoviePoster(
        modifier = modifier,
        url = posterUrl,
        posterWidth = 150.dp,
        onClick = onClick
    )
    Spacer(modifier = Modifier.width(16.dp))
}

@Composable
private fun MoviesRow(
    rowTitle: String,
    content: @Composable () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = rowTitle,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
        content()
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
        initialState = State(
            listOf(
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, false),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, false),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, false),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, false),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, false),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, false)
            )
        ),
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
                posterUrl = movie.completePosterUrl(),
                backdropUrl = movie.completeBackdropUrl(),
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
