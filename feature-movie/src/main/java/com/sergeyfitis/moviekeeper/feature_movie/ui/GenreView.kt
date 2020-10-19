package com.sergeyfitis.moviekeeper.feature_movie.ui

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.RepeatableSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.common.ext.horizontalRoundedGradientBackground
import com.sergeyfitis.moviekeeper.common.theme.MovieAppTheme
import com.sergeyfitis.moviekeeper.common.theme.gradient0
import com.sergeyfitis.moviekeeper.data.models.dto.GenreDTO

@Composable
private fun GenreItemView(genre: GenreDTO) {
    val borderWidth = with(DensityAmbient.current) { 1.dp.toPx() }
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
            val animatedAlpha = animatedFloat(initVal = 0.0f)
            if (!animatedAlpha.isRunning) {
                animatedAlpha.animateTo(
                    targetValue = 1.0f,
                    anim = RepeatableSpec(
                        iterations = AnimationConstants.Infinite,
                        animation = TweenSpec(durationMillis = 1000),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width((it * 30).dp)
                    .height(24.dp)
                    .drawLayer(alpha = animatedAlpha.value)
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
    Crossfade(current = content) {
        when(it) {
            GenreContent.STUB -> GenreContentStub()
            GenreContent.LIST -> GenreContentList(genres)
        }
    }
}

@Composable
private fun GenreContentList(genres: List<GenreDTO>) {
    LazyRowFor(
        items = genres,
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
    ) { genre ->
        GenreItemView(genre)
        Spacer(modifier = Modifier.width(8.dp))
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