package com.sergeyfitis.moviekeeper.data

import com.sergeyfitis.moviekeeper.BuildConfig

actual class ApiKeyProvider {
    actual fun getApiKey(): String = BuildConfig.API_KEY
}