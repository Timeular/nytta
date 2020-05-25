package com.timeular.nytta.http.client

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class TextHttpResponseTest : AbstractHttpResponseTest() {

    override fun createResponseWithCode(statusCode: Int): HttpResponse<Any> =
            TextHttpResponse(
                    codeValue = statusCode,
                    bodyAsText = null
            )

    @Test
    fun testBody() {
        assertThat(createTextResponse(null).body, equalTo(""))
        assertThat(createTextResponse("").body, equalTo(""))
        assertThat(createTextResponse("some text").body, equalTo("some text"))
    }

    private fun createTextResponse(body: String?) =
            TextHttpResponse(
                    codeValue = 200,
                    bodyAsText = body
            )
}