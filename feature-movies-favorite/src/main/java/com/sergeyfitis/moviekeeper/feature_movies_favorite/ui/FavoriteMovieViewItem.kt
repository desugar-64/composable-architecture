package com.sergeyfitis.moviekeeper.feature_movies_favorite.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sergeyfitis.moviekeeper.common.ui.MoviePoster
import com.sergeyfitis.moviekeeper.data.models.Category
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.sergeyfitis.moviekeeper.data.models.dto.completePosterUrl

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
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${movie.voteAverage}")
            }
        }
    }
}

@Preview(device = Devices.NEXUS_5, backgroundColor = 0xffffffL, showBackground = true)
@Composable
private fun ItemPreview() {
    FavoriteMovieViewItem(
        movie = MovieDTO(
            id = 0,
            title = "Some movie",
            poster = "",
            backdrop = "",
            voteCount = 10,
            voteAverage = 7f,
            category = Category.TOP_RATED,
            genres = emptyList(),
            overview = "A professional thief with \$40 million in debt and his family's life on the line must commit one final heist - rob a futuristic airborne casino filled with the world's most dangerous criminals."
        )
    )
}