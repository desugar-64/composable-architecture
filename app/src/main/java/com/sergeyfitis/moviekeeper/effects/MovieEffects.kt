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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import kotlin.reflect.KFunction3

private val httpMethod = prop(HttpRequestBuilder::method)
private val setHeader = prop(HttpRequestBuilder::header)
private val authToken =
    { token: String -> setHeader { "api_key" to token } }

//private val headers = prop(HttpRequestBuilder::headers)
//private val setHeader= prop(HeadersBuilder::set)
//private val authToken =
//    { token: String -> headers { setHeader { "api_key" to token }(it) } }

fun baseRequestBuilder(): HttpRequestBuilder {
    val getJson =
        HttpRequestBuilder()
            .let(authToken(BuildConfig.API_KEY) pipe httpMethod { HttpMethod.Get })

    HttpRequestBuilder()
        .let(authToken(BuildConfig.API_KEY))
        .let(httpMethod { HttpMethod.Get })

    return withA(
        HttpRequestBuilder(),
        authToken(BuildConfig.API_KEY) pipe httpMethod { HttpMethod.Get })
}

fun <Root, Value0, Value1> prop(kf: KFunction3<Root, Value0, Value1, *>): (() -> Pair<Value0, Value1>) -> (Root) -> Root {
    return { update ->
        { root ->
            val (value0, value1) = update()
            kf.invoke(root, value0, value1)
            root
        }
    }
}

private fun authInterceptor() = Interceptor { chain ->
    val url = chain
        .request()
        .url()
        .newBuilder()
        .addQueryParameter("api_key", BuildConfig.API_KEY)
        .build()
    chain.proceed(chain.request().newBuilder().url(url).build())
}

fun httpClient(vararg interceptors: Interceptor): HttpClient {
    return HttpClient(OkHttp) {
        engine { interceptors.forEach(::addInterceptor) }
        install(JsonFeature) { serializer = KotlinxSerializer() }
    }
}

inline fun <reified T> dataRequest(scope: CoroutineScope, httpClient: HttpClient): Effect<T> {

    return Effect { cl ->
        scope.launch {
            httpClient.use { cl(it.request(baseRequestBuilder())) }
        }
    }
}
