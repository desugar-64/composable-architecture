package com.sergeyfitis.moviekeeper.effects

import com.sergeyfitis.moviekeeper.prelude.withA

val getPopularMovies =
    requestBuilder() withA httpUrlLens.lift(urlPath("movie/popular"))