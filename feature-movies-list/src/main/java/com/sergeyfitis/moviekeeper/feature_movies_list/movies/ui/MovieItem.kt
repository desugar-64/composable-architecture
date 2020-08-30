package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import androidx.ui.tooling.preview.PreviewParameter
import androidx.ui.tooling.preview.PreviewParameterProvider
import com.sergeyfitis.moviekeeper.data.models.Movie

internal class MoviePreviewProvider : PreviewParameterProvider<Movie> {
    override val values: Sequence<Movie>
        get() = sequenceOf(Movie(0, "The Movie sldkfjsdlfkj sdflkjsdfs sldkfsdf", "/sdf9ds09dfs"))
}

@Preview(device = Devices.NEXUS_5, showDecoration = false)
@Composable
fun MovieItem(
    @PreviewParameter(MoviePreviewProvider::class) movie: Movie,
    modifier: Modifier = Modifier,
    onClick: (Movie) -> Unit = {}
) {
    val posterWidth = 78
    Stack(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        Surface(
            modifier = Modifier
                .preferredHeight(130.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color = Color.White)
                .gravity(Alignment.BottomStart)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        start = (posterWidth + 32).dp,
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    )
            ) {
                Column(
                    modifier = Modifier.weight(weight = 1f)
                ) {
                    Text(
                        text = movie.title,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        shape = RoundedCornerShape(4.dp),
                        backgroundColor = Color.DarkGray,
                        modifier = Modifier.size(width = 72.dp, height = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Action, Drama",
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "Rated 8000+",
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(asset = Icons.Filled.Favorite, modifier = Modifier.size(24.dp))
            }
        }
        Image(
            painter = ColorPainter(Color.Blue),
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
                .preferredWidth(posterWidth.dp)
                .aspectRatio(.6f)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}