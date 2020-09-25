package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade

@Composable
fun MoviePoster(
    modifier: Modifier = Modifier,
    url: String,
    posterWidth: Dp,
    shape: Shape = RoundedCornerShape(8.dp),
    aspectRatio: Float = 0.6f,
    onClick: () -> Unit = {}
) {
    val posterModifier = modifier
        .drawShadow(elevation = 8.dp, shape = shape)
        .width(posterWidth)
        .aspectRatio(aspectRatio)
        .background(Color.LightGray)
        .border(0.5.dp, Color.LightGray, shape)
        .clickable(onClick = onClick)

    Column {
        CoilImageWithCrossfade(
            data = url,
            contentScale = ContentScale.Crop,
            modifier = posterModifier
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Preview(backgroundColor = 0xffffffL, showBackground = true)
@Composable
internal fun PosterPreview() {
    MoviePoster(modifier = Modifier.padding(16.dp), url = "", posterWidth = 86.dp)
}