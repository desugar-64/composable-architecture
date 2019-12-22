package com.sergeyfitis.moviekeeper.statemanagement.appstate

class AppState(
    var movies: MutableList<String>,
    var favoriteMovies: Set<String>,
    var movieDetailsState: MovieDetailsState
)