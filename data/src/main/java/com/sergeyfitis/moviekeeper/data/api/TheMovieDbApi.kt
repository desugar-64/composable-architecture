package com.sergeyfitis.moviekeeper.data.api

import com.sergeyfitis.moviekeeper.data.models.MoviesResponse
import retrofit2.http.GET

interface TheMovieDbApi {
    @GET("movie/now_playing")
    suspend fun nowPlaying(): MoviesResponse

    @GET("movie/top_rated")
    suspend fun topRated(): MoviesResponse

    @GET("movie/upcoming")
    suspend fun upcoming(): MoviesResponse
}