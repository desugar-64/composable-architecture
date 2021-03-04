package com.sergeyfitis.moviekeeper.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import dev.chrisbanes.accompanist.coil.CoilImageDefaults
import dev.chrisbanes.accompanist.imageloading.toPainter

actual class ImageFetcher actual constructor() {
    @Composable
    actual fun loadImage(url: String): Painter? {
        val imageLoader = CoilImageDefaults.defaultImageLoader()
        val context = LocalContext.current
        val imageRequest = remember(key1 = url) {
            ImageRequest.Builder(context).data(url).build()
        }
        val result: Painter? by produceState(initialValue = null, key1 = imageRequest) {
            val imageResult = imageLoader.execute(imageRequest)
            imageResult.drawable?.toPainter()
        }
        return result
    }
}