package com.sergeyfitis.moviekeeper.data

expect class ApiKeyProvider constructor() {
    fun getApiKey(): String
}