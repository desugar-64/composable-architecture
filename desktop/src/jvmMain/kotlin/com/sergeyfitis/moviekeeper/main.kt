package com.sergeyfitis.moviekeeper

import androidx.compose.desktop.Window
import androidx.compose.material.Text
import androidx.compose.ui.unit.IntSize


fun main() = Window(
    title = "Movie Keeper Compose Desktop",
    size = IntSize(1280, 768),
    icon = null,
) {
   Text("Hello from Compose Desktop")
}