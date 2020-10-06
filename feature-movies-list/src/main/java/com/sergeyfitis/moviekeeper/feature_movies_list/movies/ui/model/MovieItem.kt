package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model

import androidx.compose.runtime.Immutable
import com.sergeyfitis.moviekeeper.data.models.dto.GenreDTO
import com.sergeyfitis.moviekeeper.data.models.dto.MovieDTO
import com.sergeyfitis.moviekeeper.data.models.dto.completeBackdropUrl
import com.sergeyfitis.moviekeeper.data.models.dto.completePosterUrl

@Immutable
data class MovieItem(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: Float,
    val voted: Int,
    val genres: List<GenreDTO>
)

internal fun MovieDTO.toItem(genres: List<GenreDTO>) = MovieItem(
    id = id,
    title = title,
    posterUrl = completePosterUrl(),
    backdropUrl = completeBackdropUrl(),
    rating = voteAverage,
    voted = voteCount,
    genres = genres
)