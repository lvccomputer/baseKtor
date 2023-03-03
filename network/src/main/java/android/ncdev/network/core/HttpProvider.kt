package android.ncdev.network.core

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

interface BaseUrl {
    fun getBaseUrl(): String
}

class HttpProvider(
    private val baseUrl: BaseUrl,
    private val config: (HttpClientConfig<*>.() -> Unit)? = null
) {

    constructor(url: String, config: (HttpClientConfig<*>.() -> Unit)? = null) : this(object :
        BaseUrl {
        override fun getBaseUrl() = url
    }, config)

    private val String.urlString
        get() = baseUrl.getBaseUrl().run {
            if (endsWith("/")) this else "$this/"
        } + this

    private val client: HttpClient by lazy { createHttpClient(config) }

    @Throws(Exception::class)
    suspend fun simpleGet(url: String, params: Map<String, Any?> = mapOf()): String {
        val response: String = client.get(url) {
            params.forEach {
                parameter(it.key, it.value)
            }
        }.body()
        return response
    }

    @Throws(Exception::class)
    suspend fun get(path: String, params: Map<String, Any?> = mapOf()): JsonElement {
        val response: JsonElement = client.get(path.urlString) {
            params.forEach {
                parameter(it.key, it.value)
            }
        }.body()
        return response
    }

    @Throws(Exception::class)
    suspend fun post(path: String, body: Any, headers: Map<String, String> = mapOf()): JsonObject {
        val response: JsonObject = client.post(path.urlString) {
            headers.forEach {
                header(it.key, it.value)
            }
            this.setBody(body)
        }.body()
        return response
    }

    @Throws(Exception::class)
    suspend fun put(
        path: String,
        params: Map<String, Any?> = mapOf(),
        headers: Map<String, String> = mapOf()
    ): JsonObject {
        val response: JsonObject = client.put(path.urlString) {
            setBody(buildJsonObject { })
            headers.forEach {
                header(it.key, it.value)
            }
            params.forEach {
                parameter(it.key, it.value)
            }
        }.body()
        return response
    }

    @Throws(Exception::class)
    suspend fun delete(
        path: String,
        body: Any,
        params: Map<String, Any?> = mapOf(),
        headers: Map<String, String> = mapOf()
    ): JsonObject {
        val response = client.delete(path.urlString) {
            headers.forEach {
                header(it.key, it.value)
            }
            params.forEach {
                parameter(it.key, it.value)
            }
            setBody(body)
        }.body<JsonObject>()
        return response
    }
}

private fun createHttpClient(
    config: (HttpClientConfig<*>.() -> Unit)? = null,
): HttpClient {
    return HttpClient {
        expectSuccess = false
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) = println(message)
            }
            level = LogLevel.INFO
//                     LogLevel.HEADERS
//                     LogLevel.BODY
//                     LogLevel.ALL
//                     LogLevel.NONE
        }
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 10_000
        }
        config?.let { apply(it) }
    }
}

@Throws(Exception::class)
suspend fun simpleGet(url: String, params: Map<String, Any?> = mapOf()): String {
    return createHttpClient().use {
        it.get(url) {
            params.forEach {
                parameter(it.key, it.value)
            }
        }
    }.body()
}