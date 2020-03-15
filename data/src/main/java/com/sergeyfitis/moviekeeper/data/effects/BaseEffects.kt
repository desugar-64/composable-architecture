package com.sergeyfitis.moviekeeper.data.effects

import com.sergeyfitis.moviekeeper.data.BuildConfig.*
import com.syaremych.composable_architecture.prelude.pipe
import com.syaremych.composable_architecture.prelude.types.Either
import com.syaremych.composable_architecture.prelude.types.Lens
import com.syaremych.composable_architecture.prelude.types.recover
import com.syaremych.composable_architecture.prelude.withA
import com.syaremych.composable_architecture.store.Effect
import com.syaremych.composable_architecture.store.emptyEffect
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json

private val httpMethodLens =
    Lens<HttpRequestBuilder, HttpMethod>(
        get = { it.method },
        set = { httpRequestBuilder, httpMethod ->
            httpRequestBuilder.apply { method = httpMethod }
        }
    )

val httpUrlLens =
    Lens<HttpRequestBuilder, URLBuilder>(
        get = { it.url },
        set = { httpRequestBuilder, _ -> httpRequestBuilder }
    )

private val setParamLens =
    Lens<URLBuilder, ParametersBuilder>(
        get = { it.parameters },
        set = { urlBuilder, _ -> urlBuilder }
    )

private val authToken = { token: String ->
    setParamLens.lift { it.appendMissing("api_key", listOf(token)); it }
}

fun urlPath(path: String): (URLBuilder) -> URLBuilder {
    return { it.path(API_V, path) }
}

fun requestBuilder(method: HttpMethod = HttpMethod.Get): HttpRequestBuilder {
    return withA(
        a = HttpRequestBuilder(),
        f = httpMethodLens
            .lift { method }
            .pipe(httpUrlLens.lift { it.takeFrom(BASE_URL) })
            .pipe(httpUrlLens.lift(authToken(API_KEY)))
    )
}

//fun URLBuilder.paths(vararg segments: String) = this.path(*segments)

//private val httpMethod = prop(HttpRequestBuilder::method)

//private val setParam = prop(URLBuilder::parameters)

//private val authToken =
//    { token: String -> setParam { it.append("api_key", token); it } }

//private inline val HttpRequestBuilder.urlBuilder: URLBuilder
//    get() = url

//private fun URLBuilder.takeFromString(urlString: String) = takeFrom(urlString)

//val url =
//    prop(
//        kp = HttpRequestBuilder::urlBuilder,
//        set = { newBuilder, root -> withA(newBuilder, root.url::takeFrom); root }
//    )

//val baseUrl = prop(URLBuilder::takeFromString)() { BuildConfig.BASE_URL }

//val methodGet = httpMethod { HttpMethod.Get }
//val methodPost = httpMethod { HttpMethod.Post }

//private val headers = prop(HttpRequestBuilder::headers)
//private val setHeader= prop(HeadersBuilder::set)
//private val authToken =
//    { token: String -> headers { setHeader { "api_key" to token }(it) } }

//fun baseRequestBuilder(method: HttpMethod = HttpMethod.Get): HttpRequestBuilder {
//    return withA(
//        a = HttpRequestBuilder(),
//        f = prop(HttpRequestBuilder::urlBuilder)(authToken(API_KEY)) pipe httpMethod { method })
//}

private val baseHttpClientConfig: HttpClientConfig<OkHttpConfig>.() -> Unit = {
    install(JsonFeature) {
        serializer = KotlinxSerializer(json = Json {
            ignoreUnknownKeys = true
        })
    }
}

fun httpClientInstance() = HttpClient(OkHttp, baseHttpClientConfig)

inline fun <reified T> HttpClient.dataTask(
    scope: CoroutineScope,
    requestBuilder: HttpRequestBuilder,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Effect<Either<Throwable, T>> {
    return if (scope.isActive)
        Effect { callback ->
            scope.launch {
                withContext(dispatcher) {
                    this@dataTask.use { client: HttpClient ->
                        val result =
                            Either.recover { client.request<T>(requestBuilder) }
                        callback(result)
                    }
                }
            }
        }
    else {
        emptyEffect()
    }
}
