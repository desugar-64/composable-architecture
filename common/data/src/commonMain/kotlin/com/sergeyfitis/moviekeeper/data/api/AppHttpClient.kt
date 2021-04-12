package com.sergeyfitis.moviekeeper.data.api

import com.sergeyfitis.moviekeeper.data.ApiKeyProvider
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

internal class AppHttpClient(
    private val baseUrl: String = "https://api.themoviedb.org/3/",
    private val apiKeyProvider: ApiKeyProvider = ApiKeyProvider()
) {
    private val appJson = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val authQuery = fun(builder: URLBuilder) = builder.apply {
        parameters.append(name = "api_key", value = apiKeyProvider.getApiKey())
    }

    private val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }

        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            timeout {
                requestTimeoutMillis = 100 * 1000 // 100 sec
                connectTimeoutMillis = 100 * 1000 // 100 sec
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(json = appJson)
        }
    }

    suspend inline fun <reified T> get(path: String): T {
        return client.get(
            url = URLBuilder(baseUrl).path(path).let(authQuery).build()
        )
    }

}