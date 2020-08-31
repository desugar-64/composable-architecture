package com.sergeyfitis.moviekeeper.data.api

import com.sergeyfitis.moviekeeper.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

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
    .build()