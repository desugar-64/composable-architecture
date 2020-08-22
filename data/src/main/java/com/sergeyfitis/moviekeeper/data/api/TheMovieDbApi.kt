package com.sergeyfitis.moviekeeper.data.api

import com.sergeyfitis.moviekeeper.data.models.MoviesResponse
import retrofit2.http.GET

interface TheMovieDbApi {
    @GET("movie/popular")
    suspend fun popular(): MoviesResponse
}