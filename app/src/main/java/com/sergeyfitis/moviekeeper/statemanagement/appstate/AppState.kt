package com.sergeyfitis.moviekeeper.statemanagement.appstate

import com.sergeyfitis.moviekeeper.models.Movie

class AppState(
    var movies: List<Movie>,
    var favoriteMovies: Set<String>,
    var movieDetailsState: MovieDetailsState?
)