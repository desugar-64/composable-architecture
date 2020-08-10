package com.sergeyfitis.moviekeeper.statemanagement.appstate

import android.util.Log
import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieState
import com.sergeyfitis.moviekeeper.feature_movie.state.MovieViewState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesFeatureState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.toOption

class AppState(
    var moviesFeatureState: MoviesFeatureState,
    var favoriteMovies: Set<Movie>,
    var movieState: Option<MovieState>
) {
    companion object {
        fun initial() = AppState(
            MoviesFeatureState(
                Option.empty(),
                emptyList()
            ),
            emptySet(),
            Option.empty()
        )
    }
}

// TODO: Replace with Lens
var AppState.moviesViewState: MoviesState
    get() = MoviesState(moviesFeatureState)
    set(value) {
        moviesFeatureState = value.moviesFeatureState
    }

var AppState.movieViewState: MovieViewState
    get() {
        val movie = moviesFeatureState.selectedMovie.getOrThrow()
        val isFavorite = favoriteMovies.contains(movie)
        Log.d("AppState", "get movieViewState invoked")
        return MovieViewState(
            selectedMovie = movie,
            movieState = movieState.fold({ MovieState(movie, isFavorite) }, {
                it.copy(
                    movie = movie,
                    isFavorite = isFavorite
                )
            })
        )
    }
    set(value) {
        Log.d("AppState", "set movieViewState invoked")
        movieState = value.movieState.toOption()
    }