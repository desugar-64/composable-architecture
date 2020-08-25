package com.sergeyfitis.moviekeeper.feature_movie.action

import com.sergeyfitis.moviekeeper.data.models.Movie
import kotlinx.coroutines.CoroutineScope

sealed class MovieAction {
    data class LoadDetails(val scope: CoroutineScope, val movieId: Int) : MovieAction()
    data class DetailsLoaded(val movie: Movie, val isFavorite: Boolean) : MovieAction()
    data class ToggleFavorite(val isFavorite: Boolean) : MovieAction()
}