package com.sergeyfitis.moviekeeper.feature_movies_favorite.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.FavoriteFeatureAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.FavoriteMoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.action.favoriteMoviesAction
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.FavoriteState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.ca.state.favoriteMoviesState
import com.sergeyfitis.moviekeeper.feature_movies_favorite.mock
import com.syaremych.composable_architecture.store.Store
import com.syaremych.composable_architecture.store.ViewStore
import com.syaremych.composable_architecture.store.view

@Composable
internal fun FavoriteMoviesRootView(
    modifier: Modifier = Modifier,
    viewStore: ViewStore<FavoriteState, FavoriteMoviesAction>
) {
    val state by viewStore.collectAsState(initial = viewStore.state)
    LazyColumnFor(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        items = state.favoriteMovies.mapNotNull(state.movies::get)
    ) { movie ->
        FavoriteMovieViewItem(movie = movie)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(device = Devices.NEXUS_5, backgroundColor = 0xffffffL, showBackground = true)
@Composable
private fun screenPreview() {
    FavoriteMoviesRootView(
        viewStore = Store.mock.scope(
            toLocalValue = FavoriteFeatureState.favoriteMoviesState::get,
            toGlobalAction = FavoriteFeatureAction.favoriteMoviesAction::reverseGet
        ).view
    )
}