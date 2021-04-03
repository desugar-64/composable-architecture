package com.sergeyfitis.moviekeeper.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ThemeLight = lightColors(
    primary = colorMediumOrchid,
    secondary = colorCornflowerBlue,
    onPrimary = Color.Black
)

@Composable
fun MovieAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = ThemeLight
    ) {
        content()
    }
}