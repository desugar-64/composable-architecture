package com.sergeyfitis.moviekeeper.statemanagement.appstate

import android.util.Log
import com.sergeyfitis.moviekeeper.data.models.Movie
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesState
import com.sergeyfitis.moviekeeper.feature_movies_list.movies.state.MoviesViewState
import com.syaremych.composable_architecture.prelude.types.Option
import com.syaremych.composable_architecture.prelude.types.getOrThrow
import com.syaremych.composable_architecture.prelude.types.toOption

class AppState(
    var moviesState: MoviesState,
    var favoriteMovies: Set<Movie>,
    var movieState: Option<MovieState>
) {
    companion object {
        fun initial() = AppState(
            MoviesState(
                Option.empty(),
                emptyList()
            ),
            emptySet(),
            Option.empty()
        )
    }
}

// TODO: Replace with Lens
var AppState.moviesViewState: MoviesViewState
    get() = MoviesViewState(moviesState)
    set(value) {
        moviesState = value.moviesState
    }

var AppState.movieViewState: MovieViewState
    get() {
        val movie = moviesState.selectedMovie.getOrThrow()
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