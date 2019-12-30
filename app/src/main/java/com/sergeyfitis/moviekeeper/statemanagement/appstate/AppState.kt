package com.sergeyfitis.moviekeeper.statemanagement.appstate

class AppState(
    var movies: List<String>,
    var favoriteMovies: Set<String>,
    var movieDetailsState: MovieDetailsState
)