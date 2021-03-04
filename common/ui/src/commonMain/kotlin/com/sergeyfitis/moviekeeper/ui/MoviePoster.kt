package com.sergeyfitis.moviekeeper.ui

//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sergeyfitis.moviekeeper.ext.applyIf
import com.sergeyfitis.moviekeeper.image.ImageFetcher

@Composable
fun MoviePoster(
    modifier: Modifier = Modifier,
    url: String,
    posterWidth: Dp = Dp.Unspecified,
    shape: Shape = RoundedCornerShape(8.dp),
    aspectRatio: Float = 0.6f,
    elevation: Dp = 8.dp,
    drawBorder: Boolean = true,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = Color.LightGray,
    onClick: () -> Unit = {}
) {
    // Safely update the current `onClick` lambda when a new one is provided
    val click by rememberUpdatedState(onClick)
    val imageFetcher = remember(::ImageFetcher)
    val image = imageFetcher.loadImage(url)
    val posterModifier = modifier
        .shadow(elevation = elevation, shape = shape)
        .applyIf(posterWidth != Dp.Unspecified) { requiredWidth(posterWidth) }
        .aspectRatio(aspectRatio)
        .background(backgroundColor)
        .applyIf(drawBorder) { border(0.5.dp, borderColor, shape) }
        .clickable(onClick = click)

    Column {
        if (image != null) {
            Image(
                painter = image,
                contentDescription = null,
                modifier = posterModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            Text("No Image.")
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

//@Preview(backgroundColor = 0xffffffL, showBackground = true)
@Composable
internal fun PosterPreview() {
    MoviePoster(modifier = Modifier.padding(16.dp), url = "", posterWidth = 86.dp)
}