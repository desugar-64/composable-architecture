package com.sergeyfitis.moviekeeper.feature_movie.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Text(
        modifier = Modifier
            .horizontalRoundedGradientBackground(gradient0, filled = false, borderWidth = 2f)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        text = genre.name,
        fontSize = 12.sp
    )
}

@Composable
fun GenreList(genres: List<GenreDTO>) = LazyRowFor(
    items = genres,
    contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
) { genre ->
    GenreItemView(genre)
    Spacer(modifier = Modifier.width(8.dp))
}

@Preview(device = Devices.NEXUS_5, showBackground = true, backgroundColor = 0xffffffL)
@Composable
private fun PreviewGenreList() {
    MovieAppTheme {
        GenreList(genres = listOf(GenreDTO(0, "Action"), GenreDTO(1, "Sci-Fi")))
    }
}