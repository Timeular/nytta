package com.timeular.nytta.http.client

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.assertion.assert
import okhttp3.Headers
import org.junit.jupiter.api.Test

internal class TextHttpResponseTest : AbstractHttpResponseTest() {

    override fun createResponseWithCode(statusCode: Int): HttpResponse<Any> =
            TextHttpResponse(
                    codeValue = statusCode,
                    bodyAsText = null,
                    headers = Headers.of(emptyMap())
            )

    @Test
    fun testBody() {
        assert.that(createTextResponse(null).body, equalTo(""))
        assert.that(createTextResponse("").body, equalTo(""))
        assert.that(createTextResponse("some text").body, equalTo("some text"))
    }

    private fun createTextResponse(body: String?) =
            TextHttpResponse(
                    codeValue = 200,
                    bodyAsText = body,
                    headers = Headers.of(emptyMap())
            )
}