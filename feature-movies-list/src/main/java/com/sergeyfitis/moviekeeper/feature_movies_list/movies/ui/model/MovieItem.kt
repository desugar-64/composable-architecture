package com.sergeyfitis.moviekeeper.feature_movies_list.movies.ui.model

import androidx.compose.runtime.Immutable
import com.sergeyfitis.moviekeeper.data.models.MovieDTO
import com.sergeyfitis.moviekeeper.data.models.completeBackdropUrl
import com.sergeyfitis.moviekeeper.data.models.completePosterUrl

@Immutable
data class MovieItem(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: Float,
    val voted: Int
)

internal fun MovieDTO.toItem() = MovieItem(
    id = id,
    title = title,
    posterUrl = completePosterUrl(),
    backdropUrl = completeBackdropUrl(),
    rating = voteAverage,
    voted = voteCount
)