package com.sergeyfitis.moviekeeper.data

import Movie_Keeper_Compose.common.data.BuildConfig

actual class ApiKeyProvider {
    actual fun getApiKey(): String = BuildConfig.API_KEY
}