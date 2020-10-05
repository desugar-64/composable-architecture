package com.sergeyfitis.moviekeeper.feature_movie.ui

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.FirstBaseline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.onPositioned
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.data.models.Category
import com.sergeyfitis.moviekeeper.data.models.MovieDTO
import com.sergeyfitis.moviekeeper.data.models.completePosterUrl
import com.sergeyfitis.moviekeeper.feature_movie.action.MovieAction
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.toOption
import com.syaremych.composable_architecture.store.*
import ui.MoviePoster

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
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                modifier = Modifier
                    .alignWithSiblings(alignmentLine = FirstBaseline)
                    .weight(1f, fill = true),
                text = movie.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
            val ratingShape = RoundedCornerShape(size = 6.dp)
            Text(
                modifier = Modifier
                    .alignWithSiblings(alignmentLine = FirstBaseline)
                    .background(Color.Yellow, shape = ratingShape)
                    .border(shape = ratingShape, width = 0.5f.dp, color = Color.Black)
                    .padding(horizontal = 4.dp),
                text = "IMDb ${movie.voteAverage}"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Runtime: 1h 35min")
    }
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
                category = Category.TOP_RATED
            ),
            isFavorite = true
        ).toOption(),
        reducer = Reducer.empty(),
        environment = Unit
    ).view
    MovieRootView(viewStore = viewStore)
}