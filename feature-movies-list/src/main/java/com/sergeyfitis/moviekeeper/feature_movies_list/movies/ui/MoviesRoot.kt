package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.common.ui.MoviePoster
import com.sergeyfitis.moviekeeper.data.models.dto.GenreDTO
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.actions.MoviesFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ca.stateclass.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.navigation.MovieListNavigation
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model.MovieItem
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model.toItem
import com.syaremych.composable_architecture.store.*

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
                val state by viewStore.collectAsState(viewStore.viewState.value)
                onDispose(callback = viewStore::dispose)
                val onClick: (MovieItem) -> () -> Unit = onClick@{ movie ->
                    return@onClick {
                        viewStore.send(Action.MovieTapped(movie.id))
                        navigator.openMovieDetails(movie.id)
                    }
                }

                MoviesRow(rowTitle = "Now Playing") {
                    MoviesHorizontalList(state.nowPlaying) { movieItem ->
                        MoviePosterItem(
                            posterUrl = movieItem.posterUrl,
                            onClick = onClick(movieItem)
                        )
                    }
                }
                MoviesRow(rowTitle = "Upcoming") {
                    MoviesHorizontalList(state.upcoming) { movieItem ->
                        MovieBackdropItem(item = movieItem, onClick = onClick(movieItem))
                    }
                }
                MoviesRow(rowTitle = "Top Rated") {
                    MoviesHorizontalList(state.topRated) { movieItem ->
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
        Spacer(modifier = Modifier.height(4.dp))
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
            nowPlaying = listOf(
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList())
            ),
            upcoming = listOf(
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList())
            ),
            topRated = listOf(
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList()),
                MovieItem(0, "Title", "/url", backdropUrl = "", 1f, 10, emptyList())
            )
        ),
        reducer = Reducer.empty(),
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
@Immutable
internal data class State(
    val nowPlaying: List<MovieItem>,
    val upcoming: List<MovieItem>,
    val topRated: List<MovieItem>,
) {
    companion object
}

internal sealed class Action {
    object LoadList : Action()
    data class MovieTapped(val movieId: Int) : Action()
}

private val extractGenres: (Map<Int, GenreDTO>) -> (MovieDTO) -> MovieItem =
    { genres ->
        { movie -> movie.toItem(movie.genres.mapNotNull(genres::get)) }
    }

internal fun State.Companion.init(featureState: MoviesFeatureState): State {
    val genres = featureState.genres
    return with(featureState) {
        State(
            nowPlaying = nowPlaying.mapNotNull(movies::get).map(extractGenres(genres)),
            upcoming = upcoming.mapNotNull(movies::get).map(extractGenres(genres)),
            topRated = topRated.mapNotNull(movies::get).map(extractGenres(genres))
        )
    }
}

internal fun MoviesFeatureAction.Companion.init(action: Action): MoviesFeatureAction {
    return when (action) {
        Action.LoadList -> MoviesFeatureAction.Movies(MoviesAction.LoadMovies)
        is Action.MovieTapped -> MoviesFeatureAction.Movies(
            MoviesAction.MovieTapped(action.movieId)
        )
    }
}
