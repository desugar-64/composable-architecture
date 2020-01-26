package com.sergeyfitis.moviekeeper.effects

import com.sergeyfitis.moviekeeper.prelude.withA

//private val popularMoviesPath = prop(URLBuilder::paths)() { arrayOf("3/movie/popular") }
//val getPopularMovies =
//    (baseRequestBuilder() withA url(baseUrl + popularMoviesPath))

val getPopularMovies =
    requestBuilder() withA httpUrlLens.lift(urlPath("movie/popular"))