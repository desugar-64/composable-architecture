package com.sergeyfitis.moviekeeper.feature_movie.ui

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.onPositioned
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.common.ext.horizontalRoundedGradientBackground
import com.sergeyfitis.moviekeeper.common.theme.colorGoldenTanoi
import com.sergeyfitis.moviekeeper.common.theme.gradient0
import com.sergeyfitis.moviekeeper.common.ui.MoviePoster
import com.sergeyfitis.moviekeeper.data.models.Category
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.sergeyfitis.moviekeeper.data.models.dto.completePosterUrl
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieAction
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.isEmpty
import com.syaremych.composable_architecture.prelude.types.toOption
import com.syaremych.composable_architecture.prelude.types.value
import com.syaremych.composable_architecture.store.*

@Composable
internal fun MovieRootView(viewStore: ViewStore<Option<MovieState>, MovieAction>) {
    val optionState by viewStore.collectAsState(initial = viewStore.viewState.value)
    if (optionState.isEmpty) return
    val state = optionState.value
    var posterBottomPx by mutableStateOf(0)
    Box(modifier = Modifier) {
        val movie = state.movie
        MoviePoster(
            modifier = Modifier
                .fillMaxWidth()
                .onPositioned {
                    posterBottomPx = it.size.height
                },
            url = movie.completePosterUrl(),
            shape = RoundedCornerShape(size = 0.dp),
            aspectRatio = 1.4f,
            elevation = 0.dp
        )
        val paddingTop = with(DensityAmbient.current) { posterBottomPx.toDp() }
        ScrollableColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, top = paddingTop, end = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            DetailsContent(movie)
        }
    }
}

@Composable
private fun DetailsContent(movie: MovieDTO) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f, fill = true),
                text = movie.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
            Row(
                modifier = Modifier
                    .horizontalRoundedGradientBackground(gradient0)
                    .padding(vertical = 2.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    asset = Icons.Filled.StarRate
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${movie.voteAverage}",
                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            MovieRuntimeIcon()
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Runtime: 1h 35min",
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
private fun MovieRuntimeIcon() {
    Icon(
        modifier = Modifier
            .size(18.dp)
            .drawBehind {
                val radius = 15f
                val cx = size.width / 2
                val topOffset = size.height - radius / 2
                val cy = topOffset
                drawCircle(
                    color = colorGoldenTanoi,
                    radius = radius,
                    center = Offset(cx, cy)
                )
            },
        asset = Icons.Filled.AccessTime,
    )
}

@Preview(device = Devices.NEXUS_5, showBackground = true, backgroundColor = 0x000000L)
@Composable
private fun RootPreview() {
    val viewStore = Store.init<Option<MovieState>, MovieAction, Unit>(
        initialState = MovieState(
            movie = MovieDTO(
                id = 0,
                title = "The Movie",
                poster = "/000",
                backdrop = "/111",
                voteCount = 1000,
                voteAverage = 6f,
                category = Category.TOP_RATED,
                genres = emptyList()
            ),
            isFavorite = true
        ).toOption(),
        reducer = Reducer.empty(),
        environment = Unit
    ).view
    MovieRootView(viewStore = viewStore)
}