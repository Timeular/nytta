package com.timeular.nytta.http.client

import okhttp3.Headers

class TextHttpResponse(
        codeValue: Int,
        bodyAsText: String?,
        headers: Headers = Headers.of(hashMapOf())
) : HttpResponse<String>(codeValue, headers, bodyAsText) {

    override val body = bodyAsText ?: ""
}