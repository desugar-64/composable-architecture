package com.sergeyfitis.moviekeeper.data.api

import com.sergeyfitis.moviekeeper.data.models.GenresResponse
import com.sergeyfitis.moviekeeper.data.models.MoviesResponse

internal class AppRestAPI constructor(
    private val http: AppHttpClient
) : TheMovieDbApi {

    override suspend fun nowPlaying(): MoviesResponse {
        return http.get("movie/now_playing")
    }

    override suspend fun topRated(): MoviesResponse {
        return http.get("movie/top_rated")
    }

    override suspend fun upcoming(): MoviesResponse {
        return http.get("movie/upcoming")
    }

    override suspend fun genres(): GenresResponse {
       return http.get("genre/movie/list")
    }
}