package com.sergeyfitis.moviekeeper.data.effects

import com.syaremych.composable_architecture.prelude.withA

val getPopularMovies =
    requestBuilder() withA httpUrlLens.lift(urlPath("movie/popular"))