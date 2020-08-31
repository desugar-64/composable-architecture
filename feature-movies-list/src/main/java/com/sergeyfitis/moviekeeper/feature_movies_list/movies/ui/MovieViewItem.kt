package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import androidx.ui.tooling.preview.PreviewParameter
import androidx.ui.tooling.preview.PreviewParameterProvider
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model.MovieItem
import dev.chrisbanes.accompanist.coil.CoilImageWithCrossfade

internal class MoviePreviewProvider : PreviewParameterProvider<MovieItem> {
    override val values: Sequence<MovieItem>
        get() = sequenceOf(
            MovieItem(
                id = 0,
                title = "Superman: Man of Tomorrow",
                posterUrl = "",
                rating = 4.0f,
                voted = 900,
                isFavorite = false
            )
        )
}

@Preview(device = Devices.NEXUS_5, showDecoration = false)
@Composable
fun MovieViewItem(
    @PreviewParameter(MoviePreviewProvider::class) movie: MovieItem,
    modifier: Modifier = Modifier,
    onClick: (MovieItem) -> Unit = {}
) {
    val posterWidth = 78
    Stack(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .preferredHeight(130.dp)
                .background(color = Color.White)
                .gravity(Alignment.BottomStart)
                .clickable(onClick = { onClick.invoke(movie) })
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
                    Row(verticalGravity = Alignment.CenterVertically) {
                        Icon(asset = Filled.Star, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = "${movie.rating}",
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Action, Drama",
                        style = MaterialTheme.typography.body2
                    )
                    Text(
                        text = "Voted ${movie.voted}",
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    asset = if (movie.isFavorite) Filled.Favorite else Outlined.FavoriteBorder,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        MoviePoster(
            url = movie.posterUrl,
            posterWidth = posterWidth
        )
    }
}

@Composable
private fun MoviePoster(modifier: Modifier = Modifier, url: String, posterWidth: Int) {
    val posterShape = RoundedCornerShape(4.dp)
    val posterModifier = modifier
        .padding(start = 16.dp, bottom = 16.dp)
        .preferredWidth(posterWidth.dp)
        .aspectRatio(.6f)
        .drawShadow(elevation = 8.dp, shape = posterShape)
        .background(Color.LightGray)
        .border(0.5.dp, Color.LightGray, posterShape)
        .clip(posterShape)

    CoilImageWithCrossfade(
        data = url,
        contentScale = ContentScale.Crop,
        modifier = posterModifier
    )
}