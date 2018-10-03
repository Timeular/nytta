package com.timeular.nytta.http.client

import com.google.gson.JsonElement

interface HttpClient {

    fun request(
            httpMethod: HttpMethod,
            url: String,
            bodyJson: JsonElement? = null,
            headers: Map<String, String> = emptyMap()
    ): JsonHttpResponse

    fun requestWithTextResponse(
            httpMethod: HttpMethod,
            url: String,
            bodyJson: JsonElement? = null,
            headers: Map<String, String> = emptyMap()
    ): TextHttpResponse
}