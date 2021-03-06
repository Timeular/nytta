package com.timeular.nytta.http.client

import okhttp3.Headers

/**
 * http repsonse which returns the plain body in text form without modifying it
 */
class TextHttpResponse(
        codeValue: Int,
        bodyAsText: String?,
        headers: Headers = Headers.Builder().build()
) : HttpResponse<String>(codeValue, headers, bodyAsText) {

    override val body = bodyAsText ?: ""
}
