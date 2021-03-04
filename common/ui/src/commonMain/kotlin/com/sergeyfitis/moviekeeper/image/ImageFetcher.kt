package com.sergeyfitis.moviekeeper.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

expect class ImageFetcher() {
    @Composable
    fun loadImage(url: String): Painter?
}