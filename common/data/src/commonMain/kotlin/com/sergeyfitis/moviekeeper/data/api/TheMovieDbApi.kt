package com.sergeyfitis.moviekeeper.data.api

import com.sergeyfitis.moviekeeper.data.models.GenresResponse
import com.sergeyfitis.moviekeeper.data.models.MoviesResponse

interface TheMovieDbApi {
    suspend fun nowPlaying(): MoviesResponse
    suspend fun topRated(): MoviesResponse
    suspend fun upcoming(): MoviesResponse
    suspend fun genres(): GenresResponse
}