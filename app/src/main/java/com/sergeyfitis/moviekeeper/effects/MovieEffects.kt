package com.sergeyfitis.moviekeeper.effects

import com.sergeyfitis.moviekeeper.prelude.plus
import com.sergeyfitis.moviekeeper.prelude.withA
import io.ktor.http.URLBuilder

val latestMoviesPath = prop(URLBuilder::paths)() { arrayOf("movies/last") }
val getLatestMovies =
    (baseRequestBuilder() withA url(baseUrl + latestMoviesPath))

// url(baseUrl map { it.path("movies/last") })