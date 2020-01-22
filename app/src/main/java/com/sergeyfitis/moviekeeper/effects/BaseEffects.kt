package com.sergeyfitis.moviekeeper.effects

import com.sergeyfitis.moviekeeper.BuildConfig
import com.sergeyfitis.moviekeeper.prelude.pipe
import com.sergeyfitis.moviekeeper.prelude.prop
import com.sergeyfitis.moviekeeper.prelude.withA
import com.sergeyfitis.moviekeeper.statemanagement.store.Effect
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private val httpMethod = prop(HttpRequestBuilder::method)
private val setParam = prop(URLBuilder::parameters)
private val authToken =
    { token: String -> setParam { it.append("api_key", token); it } }

private inline val HttpRequestBuilder.urlBuilder: URLBuilder
        get() = url

private fun URLBuilder.takeFromString(urlString: String) = takeFrom(urlString)

fun URLBuilder.paths(vararg segments: String) = this.path(*segments)

val url =
    prop(
        kp = HttpRequestBuilder::urlBuilder,
        set = { newBuilder, root -> withA(newBuilder, root.url::takeFrom); root }
    )

val baseUrl = prop(URLBuilder::takeFromString)(){ BuildConfig.BASE_URL }

val methodGet = httpMethod { HttpMethod.Get }
val methodPost = httpMethod { HttpMethod.Post }

//private val headers = prop(HttpRequestBuilder::headers)
//private val setHeader= prop(HeadersBuilder::set)
//private val authToken =
//    { token: String -> headers { setHeader { "api_key" to token }(it) } }

fun baseRequestBuilder(method: HttpMethod = HttpMethod.Get): HttpRequestBuilder {
    return withA(
        a = HttpRequestBuilder(),
        f = prop(HttpRequestBuilder::urlBuilder)(authToken(BuildConfig.API_KEY)) pipe  httpMethod { method })
}

fun httpClient(): HttpClient {
    return HttpClient(OkHttp) {
        install(JsonFeature) { serializer = KotlinxSerializer(json = Json.nonstrict) }
    }
}

inline fun <reified T> HttpClient.dataTask(scope: CoroutineScope, requestBuilder: HttpRequestBuilder): Effect<T> {
    return Effect { callback ->
        scope.launch {
            withContext(Dispatchers.IO) {
                this@dataTask.use { withA(it.request(requestBuilder), callback) }
            }
        }
    }
}
