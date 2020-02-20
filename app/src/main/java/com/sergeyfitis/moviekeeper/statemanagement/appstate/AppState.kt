package com.sergeyfitis.moviekeeper.statemanagement.appstate

import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.prelude.types.Option
import com.sergeyfitis.moviekeeper.prelude.types.getOrElse
import com.sergeyfitis.moviekeeper.prelude.types.getOrThrow
import com.sergeyfitis.moviekeeper.prelude.types.toOption

class AppState(
    var selectedMovie: Option<Movie>,
    var movies: List<Movie>,
    var favoriteMovies: Set<Movie>,
    var movieState: Option<MovieState>
)

var AppState.movieViewState: MovieViewState
    get() {
        val movie = selectedMovie.getOrThrow()
        return MovieViewState(
            selectedMovie = movie,
            movieState = movieState.getOrElse { MovieState(movie, favoriteMovies.contains(movie)) }
        )
    }
    set(value) {
        movieState = value.movieState.toOption()
    }