package com.sergeyfitis.moviekeeper.statemanagement.appstate

import com.sergeyfitis.moviekeeper.models.Movie
import com.sergeyfitis.moviekeeper.prelude.types.Either

class AppState(
    var movies: List<Movie>,
    var favoriteMovies: Set<Movie>,
    var movieState: Either<Unit, MovieState>
)