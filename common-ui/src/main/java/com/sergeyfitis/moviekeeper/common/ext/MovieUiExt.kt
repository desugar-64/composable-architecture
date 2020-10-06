package com.sergeyfitis.moviekeeper.common.ext

import androidx.compose.ui.Modifier
import androidx.compose.ui.drawWithCache
import androidx.compose.ui.geometry.Radius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.HorizontalGradient
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke

fun Modifier.applyIf(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
) = if (condition) modifier() else this

fun Modifier.roundRectBackground(color: Color): Modifier = drawWithCache {
    val cornerRadius = size.height / 2
    onDraw {
        drawRoundRect(color = color, radius = Radius(cornerRadius, cornerRadius))
    }
}

fun Modifier.horizontalRoundedGradientBackground(colors: List<Color>, filled: Boolean = true, borderWidth: Float = 0f): Modifier = drawWithCache {
    val cornerRadius = size.height / 2
    val gradient = HorizontalGradient(colors = colors, 0f, size.width)
    onDraw {
        val style = if (filled) Fill else Stroke(width = borderWidth)
        drawRoundRect(brush = gradient, radius = Radius(cornerRadius, cornerRadius), style = style)
    }
}