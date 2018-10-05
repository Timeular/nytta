package com.timeular.nytta.http.client

import com.google.gson.JsonElement

/**
 * Main http client abstraction for easier exchanging the httpCLient (e.g. for testing)
 */
interface HttpClient {

    /**
     * request with intents to have json response
     */
    fun request(
            httpMethod: HttpMethod,
            url: String,
            bodyJson: JsonElement? = null,
            headers: Map<String, String> = emptyMap()
    ): JsonHttpResponse

    /**
     * request with intents to have a text response
     */
    fun requestWithTextResponse(
            httpMethod: HttpMethod,
            url: String,
            bodyJson: JsonElement? = null,
            headers: Map<String, String> = emptyMap()
    ): TextHttpResponse
}