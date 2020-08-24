package com.sergeyfitis.moviekeeper.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sergeyfitis.moviekeeper.data.BuildConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
private val baseRetrofit: Retrofit = Retrofit
    .Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .client(baseOkHttpClient)
    .addConverterFactory(
        Json { ignoreUnknownKeys = true }
            .asConverterFactory(MediaType.parse("json/application")!!)
    )
    .build()

internal val moviesApi: TheMovieDbApi = baseRetrofit.create(TheMovieDbApi::class.java)