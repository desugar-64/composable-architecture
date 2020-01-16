package com.sergeyfitis.moviekeeper.effects

import com.sergeyfitis.moviekeeper.BuildConfig
import com.sergeyfitis.moviekeeper.statemanagement.store.Effect
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.headers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor

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
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
}

inline fun <reified T> dataRequest(scope: CoroutineScope, httpClient: HttpClient): Effect<T> {

    return Effect { cl ->
        scope.launch {
            httpClient.get<Unit>() {
                this.headers {

                }
            }
            httpClient.use { cl(it.get()) }
        }
    }
}
