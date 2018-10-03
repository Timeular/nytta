package com.timeular.nytta.http.client

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.Headers

class JsonHttpResponse(
        codeValue: Int,
        bodyAsText: String?,
        headers: Headers = Headers.of(hashMapOf())
) : HttpResponse<JsonElement>(codeValue, headers, bodyAsText) {

    override val body: JsonElement by lazy { JsonParser().parse(bodyAsText ?: "") }
}