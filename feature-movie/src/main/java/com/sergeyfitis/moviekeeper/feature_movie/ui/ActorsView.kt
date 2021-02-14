package com.sergeyfitis.moviekeeper.feature_movie.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.common.theme.MovieAppTheme
import com.sergeyfitis.moviekeeper.common.ui.MoviePoster

@Composable
fun ActorsList(actors: List<String>) {
    LazyRow {
        items(items = actors) { actor ->
            ActorViewItem(actor = actor)
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
private fun ActorViewItem(actor: String) {
    val cellWidth = 100.dp
    Column(modifier = Modifier.width(cellWidth)) {
        MoviePoster(url = "", aspectRatio = 1f, posterWidth = cellWidth, elevation = 0.1.dp)
        Text(
            modifier = Modifier.wrapContentWidth(),
            text = actor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(device = Devices.NEXUS_5, backgroundColor = 0xffffffffL, showBackground = true)
@Composable
private fun ActorsPreview() {
    MovieAppTheme {
        ActorsList(actors = listOf("Gal Gadot", "Actor", "Long Long actor name"))
    }
}