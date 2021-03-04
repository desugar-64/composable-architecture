package com.sergeyfitis.moviekeeper.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val ThemeLight = lightColors(
    primary = colorMediumOrchid,
    secondary = colorCornflowerBlue,

)

@Composable
fun MovieAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = ThemeLight
    ) {
        content()
    }
}