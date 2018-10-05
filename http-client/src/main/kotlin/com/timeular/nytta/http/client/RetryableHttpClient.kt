package com.timeular.nytta.http.client

import com.google.gson.JsonElement
import org.springframework.retry.support.RetryTemplate

/**
 * Retryable http client which intents to retries a http call based on
 * the configured retryTemplate.
 *
 * Note: This is only a wrapper implementation and works with any other
 * {@link HttpClient} implementation.
*/
class RetryableHttpClient(
        private val httpClientDelegate: HttpClient,
        private val retryTemplate: RetryTemplate
) : HttpClient {
    override fun request(
            httpMethod: HttpMethod,
            url: String,
            bodyJson: JsonElement?,
            headers: Map<String, String>
    ): JsonHttpResponse =
            retryTemplate.execute<JsonHttpResponse, Throwable> {
                val response = httpClientDelegate.request(
                        httpMethod = httpMethod,
                        url = url,
                        bodyJson = bodyJson,
                        headers = headers
                )

                checkAndThrowServerErrors(
                        httpMethod,
                        url,
                        response
                )

                response
            }

    override fun requestWithTextResponse(
            httpMethod: HttpMethod,
            url: String,
            bodyJson: JsonElement?,
            headers: Map<String, String>
    ): TextHttpResponse =
            retryTemplate.execute<TextHttpResponse, Throwable> {
                val response = httpClientDelegate.requestWithTextResponse(
                        httpMethod = httpMethod,
                        url = url,
                        bodyJson = bodyJson,
                        headers = headers
                )

                checkAndThrowServerErrors(
                        httpMethod,
                        url,
                        response
                )

                response
            }

    private fun checkAndThrowServerErrors(
            httpMethod: HttpMethod,
            url: String,
            response: HttpResponse<Any>
    ) {
        if (response.isServerError()) {
            throw HttpClientException(
                    "An unexpected server error occurred while calling $httpMethod - $url: ${response.code}"
            )
        }
    }
}

class HttpClientException(message: String) : RuntimeException(message)