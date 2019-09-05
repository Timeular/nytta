package com.timeular.nytta.http.client

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.Headers

/**
 * response which parses the response to json
 *
 * Hint: the response will be parsed in a lazy approach - that means it will be parsed
 * when the body is accessed 
 */
class JsonHttpResponse(
        codeValue: Int,
        bodyAsText: String?,
        headers: Headers = Headers.Builder().build()
) : HttpResponse<JsonElement>(codeValue, headers, bodyAsText) {

    override val body: JsonElement by lazy { JsonParser().parse(bodyAsText ?: "") }
}
