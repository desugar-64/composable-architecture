package com.sergeyfitis.moviekeeper.feature_movies_favorite.ui

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.HorizontalGradient
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.data.models.Movie
import ui.MoviePoster

@Composable
fun FavoriteMovieViewItem(movie: Movie) {
    val outerShape = RoundedCornerShape(16.dp)
    val innerShape = RoundedCornerShape(8.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(outerShape)
            .drawBehind {
                drawRect(
                    HorizontalGradient(
                        colorStops = arrayOf(
                            0f to Color.Blue,
                            size.height / 2 to Color.Transparent
                        ),
                        startX = 0f,
                        endX = size.width
                    )
                )
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoviePoster(url = movie.poster, posterWidth = 56.dp, shape = innerShape)
        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
            Text(
                text = movie.title,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(asset = Icons.Filled.Star, modifier = Modifier.size(16.dp), tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${movie.voteAverage}", color = Color.White)
            }
        }
    }
}

@Preview(device = Devices.NEXUS_5, backgroundColor = 0xffffffL, showBackground = true)
@Composable
private fun ItemPreview() {
    FavoriteMovieViewItem(movie = Movie(0, "Some movie", "", 10, 7f))
}