package com.timeular.nytta.http.client

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.Headers

/**
 * response which parses the response to json
 *
 * Hint: the response will parsed in a lazy approach - that means it will be parsed
 * when you accessing the body
 */
class JsonHttpResponse(
        codeValue: Int,
        bodyAsText: String?,
        headers: Headers = Headers.of(hashMapOf())
) : HttpResponse<JsonElement>(codeValue, headers, bodyAsText) {

    override val body: JsonElement by lazy { JsonParser().parse(bodyAsText ?: "") }
}