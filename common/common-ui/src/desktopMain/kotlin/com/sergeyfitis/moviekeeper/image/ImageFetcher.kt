package com.sergeyfitis.moviekeeper.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import io.kamel.core.Resource
import io.kamel.image.lazyImageResource

actual class ImageFetcher actual constructor(){
    @Composable
    actual fun loadImage(url: String): Painter? {
        return when(val imageResource = lazyImageResource(url)) {
            is Resource.Success -> BitmapPainter(imageResource.value)
            else -> null
        }
    }
}