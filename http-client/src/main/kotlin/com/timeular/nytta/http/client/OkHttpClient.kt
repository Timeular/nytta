package com.timeular.nytta.http.client

import com.google.gson.JsonElement
import com.timeular.nytta.http.client.HttpMethod.*
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * {@link HttpClient} implementation which wraps okHttp client
 */
open class OkHttpClient(
    private val okHttpClient: okhttp3.OkHttpClient,
    private val logSlowRequests: Boolean = false,
    private val slowRequestLimitInSeconds: Long = 9
) : HttpClient {

    constructor(
        timeoutInSeconds: Long = 10,
        logSlowRequests: Boolean = false,
        slowRequestLimitInSeconds: Long = 9
    ) : this(
        okHttpClient = okhttp3.OkHttpClient.Builder()
            .connectTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .build(),
        logSlowRequests = logSlowRequests,
        slowRequestLimitInSeconds = slowRequestLimitInSeconds
    )

    private companion object {
        private val logger = LoggerFactory.getLogger(OkHttpClient::class.java)
    }

    override fun request(
            httpMethod: HttpMethod,
            url: String,
            bodyJson: JsonElement?,
            headers: Map<String, String>
    ): JsonHttpResponse =
            makeRequest(bodyJson, headers, httpMethod, url) { respCode, respBody, respHeaders ->
                JsonHttpResponse(
                        codeValue = respCode,
                        bodyAsText = respBody?.string(),
                        headers = respHeaders
                )
            }

    override fun requestWithTextResponse(
            httpMethod: HttpMethod,
            url: String,
            bodyJson: JsonElement?,
            headers: Map<String, String>
    ): TextHttpResponse =
            makeRequest(bodyJson, headers, httpMethod, url) { respCode, respBody, respHeaders ->
                TextHttpResponse(
                        codeValue = respCode,
                        bodyAsText = respBody?.string(),
                        headers = respHeaders
                )
            }

    open fun <T> makeRequest(
            bodyJson: JsonElement?,
            headers: Map<String, String>,
            httpMethod: HttpMethod,
            url: String,
            factory: (
                    Int,
                    ResponseBody?,
                    Headers
            ) -> T
    ): T {
        val body = if (bodyJson == null) emptyRequestBodyFor(httpMethod)
        else requestBodyWith(bodyJson.toString())

        logger.debug("Calling {} | {}", httpMethod.name, url)

        if (logger.isTraceEnabled) {
            logger.trace(
                    """
            |Make a HTTP request:
            |- method  : ${httpMethod.name}
            |- url     : $url
            |- headers : (below)
            |${headers.entries.fold("") { log, header -> "$log\n${header.key} : ${header.value}" }}
            |- body    : (below)
            |${bodyJson?.toString() ?: ""}
            """.trimMargin()
            )
        }

        val request = okhttp3.Request.Builder()
                .url(url)
                .headers(headers.toHeader())
                .method(httpMethod.name, body)
                .build()

        val start = Instant.now()
        val response = try {
            okHttpClient.newCall(request).execute()
        } finally {
            val stop = Instant.now()
            if (logSlowRequests) {
                val duration = (stop.toEpochMilli() - start.toEpochMilli()) / 1000
                if (duration >= slowRequestLimitInSeconds) {
                    logger.warn(
                            "Slow Request Detected - URL: {} - {}, Duration: {}s",
                            request.method,
                            request.url,
                            duration
                    )
                }
            }
        }

        if (logger.isTraceEnabled) {
            logger.trace(
                    """
            | HTTP response:
            |- url     : ${response.request.method} - ${response.request.url}
            |- code    : ${response.code}
            |- headers : (below)
            |${response.headers}
            |- body    : (below)
            | can just be read once
            """.trimMargin()
            )
        }

        return createResponse(response, factory)
    }

    private fun <T> createResponse(
            response: okhttp3.Response,
            factory: (
                    Int,
                    ResponseBody?,
                    Headers
            ) -> T
    ): T = response.body?.use { body ->
        factory(
                response.code,
                body,
                response.headers
        )
    } ?: factory(
            response.code,
            null,
            response.headers
    )

    private fun emptyRequestBodyFor(httpMethod: HttpMethod): okhttp3.RequestBody? =
            when (httpMethod) {
                POST, PATCH, PUT -> requestBodyWith("")
                else -> null
            }

    open fun requestBodyWith(bodyJsonAsText: String): okhttp3.RequestBody? =
            bodyJsonAsText.toRequestBody("application/json".toMediaType())

    private fun Map<String, String>.toHeader(): Headers {
        val builder = Headers.Builder()
        this.forEach {
            builder.add(it.key, it.value)
        }
        return builder.build()
    }
}
