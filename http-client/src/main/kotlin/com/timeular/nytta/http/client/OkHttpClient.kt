package com.timeular.nytta.http.client

import com.google.gson.JsonElement
import com.timeular.nytta.http.client.HttpMethod.*
import okhttp3.Headers
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * {@link HttpClient} implementation which wraps okHttp client
 */
open class OkHttpClient(
        val timeoutInSeconds: Long = 10
) : HttpClient {

    private companion object {
        private val logger = LoggerFactory.getLogger(OkHttpClient::class.java)
    }

    private val okHttpClient: okhttp3.OkHttpClient = okhttp3.OkHttpClient.Builder()
            .connectTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutInSeconds, TimeUnit.SECONDS)
            .build()

    override fun request(
            httpMethod: HttpMethod,
            url: String,
            bodyJson: JsonElement?,
            headers: Map<String, String>
    ): JsonHttpResponse =
            makeRequest(bodyJson, headers, httpMethod, url) { respCode, respBody, respHeaders ->
                JsonHttpResponse(
                        codeValue = respCode,
                        bodyAsText = respBody,
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
                        bodyAsText = respBody,
                        headers = respHeaders
                )
            }

    private fun <T> makeRequest(
            bodyJson: JsonElement?,
            headers: Map<String, String>,
            httpMethod: HttpMethod,
            url: String,
            factory: (
                    Int,
                    String?,
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
                .headers(okhttp3.Headers.of(headers))
                .method(httpMethod.name, body)
                .build()
        val response = okHttpClient.newCall(request).execute()

        if (logger.isTraceEnabled) {
            logger.trace(
                    """
            | HTTP response:
            |- url     : ${response?.request()?.method()} - ${response?.request()?.url()}
            |- code    : ${response?.code()}
            |- headers : (below)
            |${response?.headers()?.toString()}
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
                    String?,
                    Headers
            ) -> T
    ): T = response.body()?.use { body ->
        factory(
                response.code(),
                body.string(),
                response.headers()
        )
    } ?: factory(
            response.code(),
            null,
            response.headers()
    )

    private fun emptyRequestBodyFor(httpMethod: HttpMethod): okhttp3.RequestBody? =
            when (httpMethod) {
                POST, PATCH, PUT -> requestBodyWith("")
                else -> null
            }

    private fun requestBodyWith(bodyJsonAsText: String): okhttp3.RequestBody? =
            okhttp3.RequestBody.create(
                    okhttp3.MediaType.parse("application/json"),
                    bodyJsonAsText
            )
}
