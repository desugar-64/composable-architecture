package com.sergeyfitis.moviekeeper

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.ui.MoviePoster


fun main() = Window(
    title = "Movie Keeper Compose Desktop",
    size = IntSize(640, 480),
    icon = null,
) {
    Column {
        Text("Hello from Compose Desktop")
        MoviePoster(posterWidth = 200.dp, url = "https://upload.wikimedia.org/wikipedia/en/1/14/Tenet_movie_poster.jpg")

    }
}