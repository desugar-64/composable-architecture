package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model.MovieItem

private val movie = MovieItem(
    id = 0,
    title = "Superman: Man of Tomorrow",
    posterUrl = "",
    backdropUrl = "",
    rating = 4.0f,
    voted = 900,
    isFavorite = false
)

@Preview(device = Devices.NEXUS_5, showDecoration = false)
@Composable
fun MovieViewItemPreview() {
    MovieViewItem(item = movie)
}