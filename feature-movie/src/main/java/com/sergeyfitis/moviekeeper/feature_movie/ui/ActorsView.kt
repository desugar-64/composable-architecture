package com.sergeyfitis.moviekeeper.feature_movie.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.common.theme.MovieAppTheme
import com.sergeyfitis.moviekeeper.common.ui.MoviePoster

@Composable
fun ActorsList(actors: List<String>) {
    LazyRowFor(items = actors) { actor ->
        ActorViewItem(actor = actor)
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
private fun ActorViewItem(actor: String) {
    val cellWidth = 100.dp
    Column(modifier = Modifier.width(cellWidth), horizontalAlignment = Alignment.Start) {
        MoviePoster(url = "", aspectRatio = 1f, posterWidth = cellWidth, elevation = 0.1.dp)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = actor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = Color.LightGray
        )
    }
}

@Preview(device = Devices.NEXUS_5, backgroundColor = 0xffffffffL, showBackground = true)
@Composable
private fun ActorsPreview() {
    MovieAppTheme {
        ActorsList(actors = listOf("Gal Gadot", "Tom Nusi", "Connie Nielse"))
    }
}