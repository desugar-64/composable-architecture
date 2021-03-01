package com.sergeyfitis.moviekeeper.data.api

import Movie_Keeper_Compose.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

private val authQueryAppenderInterceptor: Interceptor = Interceptor { chain ->
    val requestBuilder = chain.request().newBuilder()
    val url = chain.request().url
    val urlBuilder = url.newBuilder()
    if (url.queryParameter("api_key") == null) {
        urlBuilder.addQueryParameter("api_key", BuildConfig.API_KEY)
    }
    chain.proceed(
        requestBuilder
            .url(urlBuilder.build())
            .build()
    )
}

internal val baseOkHttpClient: OkHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(authQueryAppenderInterceptor)
    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    .readTimeout(100, TimeUnit.SECONDS)
    .writeTimeout(100, TimeUnit.SECONDS)
    .connectTimeout(100, TimeUnit.SECONDS)
    .build()