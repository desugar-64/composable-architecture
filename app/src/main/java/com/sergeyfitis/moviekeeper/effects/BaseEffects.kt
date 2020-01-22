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
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction2

private val httpMethod = prop(HttpRequestBuilder::method)
private val setHeader = prop(HttpRequestBuilder::header)
private val authToken =
    { token: String -> setHeader { "api_key" to token } }

private inline val HttpRequestBuilder.urlBuilder: URLBuilder
        get() = url

private fun URLBuilder.takeFromString(urlString: String) = takeFrom(urlString)

fun URLBuilder.paths(vararg segments: String) = this.path(*segments)

val url =
    prop(
        kp = HttpRequestBuilder::urlBuilder,
        set = { newBuilder, root -> withA(newBuilder, root.url::takeFrom); root }
    )

val baseUrl = prop(URLBuilder::takeFromString)(){ BuildConfig.BASE_URL } pipe prop(URLBuilder::paths)() { arrayOf("3") }

fun <Root, Value> prop(kf: KFunction2<Root, Value, Root>): (() -> Value) -> (Root) -> Root {
    return { update ->
        { root ->
            kf.invoke(root, update())
        }
    }
}

val methodGet = httpMethod { HttpMethod.Get }
val methodPost = httpMethod { HttpMethod.Post }

//private val headers = prop(HttpRequestBuilder::headers)
//private val setHeader= prop(HeadersBuilder::set)
//private val authToken =
//    { token: String -> headers { setHeader { "api_key" to token }(it) } }

fun baseRequestBuilder(method: HttpMethod = HttpMethod.Get): HttpRequestBuilder {
    return withA(
        a = HttpRequestBuilder(),
        f = authToken(BuildConfig.API_KEY) pipe httpMethod { method })
}

fun httpClient(): HttpClient {
    return HttpClient(OkHttp) {
        install(JsonFeature) { serializer = KotlinxSerializer() }
    }
}

inline fun <reified T> HttpClient.dataTask(scope: CoroutineScope, requestBuilder: HttpRequestBuilder): Effect<T> {
    return Effect { callback ->
        scope.launch {
            this@dataTask.use { withA(it.request(requestBuilder), callback) }
        }
    }
}
