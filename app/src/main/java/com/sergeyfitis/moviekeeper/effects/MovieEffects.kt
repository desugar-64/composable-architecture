package com.sergeyfitis.moviekeeper.effects

import com.sergeyfitis.moviekeeper.prelude.plus
import com.sergeyfitis.moviekeeper.prelude.withA
import io.ktor.http.URLBuilder

private val popularMoviesPath = prop(URLBuilder::paths)() { arrayOf("3/movie/popular") }
val getPopularMovies =
    (baseRequestBuilder() withA url(baseUrl + popularMoviesPath))

// url(baseUrl map { it.path("movies/last") })