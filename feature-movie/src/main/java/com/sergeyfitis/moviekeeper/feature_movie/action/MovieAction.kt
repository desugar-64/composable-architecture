package com.sergeyfitis.moviekeeper.feature_movie.action

import com.sergeyfitis.moviekeeper.data.models.Movie
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.Prism
import com.syaremych.composable_architecture.prelude.types.toOption
import kotlinx.coroutines.CoroutineScope

sealed class MovieAction {
    data class LoadDetails(val scope: CoroutineScope, val movieId: Int) : MovieAction()
    data class DetailsLoaded(val movie: Movie, val isFavorite: Boolean) : MovieAction()
    data class Favorite(val movie: Movie) : MovieAction()
    data class UnFavorite(val movie: Movie) : MovieAction()
}