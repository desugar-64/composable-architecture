package com.sergeyfitis.moviekeeper.ext

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke

inline fun Modifier.applyIf(
    condition: Boolean,
    modifier: Modifier.() -> Modifier
) = if (condition) this.then(modifier()) else this

fun Modifier.roundRectBackground(color: Color): Modifier = drawWithCache {
    val cornerRadius = size.height / 2
    onDrawBehind {
        drawRoundRect(color = color, cornerRadius = CornerRadius(cornerRadius, cornerRadius))
    }
}

fun Modifier.horizontalRoundedGradientBackground(
    colors: List<Color>,
    filled: Boolean = true,
    borderWidth: Float = 0f
): Modifier = drawWithCache {
    val cornerRadius = size.height / 2
    val gradient =
        horizontalGradient(colors = colors, endX = size.width)
    onDrawBehind {
        val style = if (filled) Fill else Stroke(width = borderWidth)
        drawRoundRect(
            brush = gradient,
            cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            style = style
        )
    }
}