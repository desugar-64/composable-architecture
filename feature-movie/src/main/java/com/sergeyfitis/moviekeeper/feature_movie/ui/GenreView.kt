package com.sergeyfitis.moviekeeper.feature_movie.ui

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sergeyfitis.moviekeeper.common.ext.horizontalRoundedGradientBackground
import com.sergeyfitis.moviekeeper.common.theme.MovieAppTheme
import com.sergeyfitis.moviekeeper.common.theme.gradient0
import com.sergeyfitis.moviekeeper.data.models.dto.GenreDTO

@Composable
private fun GenreItemView(genre: GenreDTO) {
    val borderWidth = with(LocalDensity.current) { 1.dp.toPx() }
    Text(
        modifier = Modifier
            .padding(1.dp)
            .horizontalRoundedGradientBackground(
                gradient0,
                filled = false,
                borderWidth = borderWidth
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = genre.name,
        fontSize = 12.sp
    )
}

@Composable
fun GenreContentStub() {
    Row {
        (2..4).forEach {
            val animatedAlpha = remember { Animatable(initialValue = 0.0f) }
            if (!animatedAlpha.isRunning) {
                LaunchedEffect(animatedAlpha) {
                    animatedAlpha.animateTo(
                        targetValue = 1.0f,
                        animationSpec = InfiniteRepeatableSpec(
                            animation = TweenSpec(durationMillis = 1000),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width((it * 30).dp)
                    .height(24.dp)
                    .alpha(animatedAlpha.value)
                    .background(color = Color.LightGray, shape = RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))

        }
    }
}

private enum class GenreContent { STUB, LIST }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GenreList(genres: List<GenreDTO>) {
    Log.d("GenreList", "render")
    val content = if (genres.isEmpty()) GenreContent.STUB else GenreContent.LIST
    Crossfade(targetState = content) {
        when (it) {
            GenreContent.STUB -> GenreContentStub()
            GenreContent.LIST -> GenreContentList(genres)
        }
    }
}

@Composable
private fun GenreContentList(genres: List<GenreDTO>) {
    LazyRow(
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
    ) {
        items(items = genres) { genre ->
            GenreItemView(genre)
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Preview(device = Devices.NEXUS_5, showBackground = true, backgroundColor = 0xffffffL)
@Composable
private fun PreviewGenreList() {
    MovieAppTheme {
        GenreList(genres = listOf(GenreDTO(0, "Action"), GenreDTO(1, "Sci-Fi")))
//        GenreList(genres = listOf())
    }
}