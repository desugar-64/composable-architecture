package com.sergeyfitis.moviekeeper.feature_movie.action

import com.sergeyfitis.moviekeeper.data.models.Movie
import kotlinx.coroutines.CoroutineScope

sealed class MovieAction {
    data class GetDetails(val scope: CoroutineScope, val movieId: Int) : MovieAction()
    data class Loaded(val movie: Movie, val isFavorite: Boolean) : MovieAction()
}