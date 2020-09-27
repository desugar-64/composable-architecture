package com.sergeyfitis.moviekeeper.feature_movies_favorite.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Devices
import androidx.ui.tooling.preview.Preview
import com.sergeyfitis.moviekeeper.data.models.Category
import com.sergeyfitis.moviekeeper.data.models.MovieDTO
import com.sergeyfitis.moviekeeper.data.models.completePosterUrl
import ui.MoviePoster

@Composable
fun FavoriteMovieViewItem(movie: MovieDTO) {
    val outerShape = RoundedCornerShape(16.dp)
    val innerShape = RoundedCornerShape(8.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(border = BorderStroke(.5.dp, Color.Black), shape = outerShape)
            .clip(outerShape)
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoviePoster(url = movie.completePosterUrl(), posterWidth = 56.dp, shape = innerShape)
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Text(
                text = movie.title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(asset = Icons.Filled.Star, modifier = Modifier.size(12.dp), tint = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${movie.voteAverage}")
            }
        }
    }
}

@Preview(device = Devices.NEXUS_5, backgroundColor = 0xffffffL, showBackground = true)
@Composable
private fun ItemPreview() {
    FavoriteMovieViewItem(movie = MovieDTO(0, "Some movie", "", "", 10, 7f, Category.TOP_RATED))
}