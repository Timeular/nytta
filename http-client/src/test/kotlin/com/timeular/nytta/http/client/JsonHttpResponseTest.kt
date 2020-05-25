package com.timeular.nytta.http.client

import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class JsonHttpResponseTest : AbstractHttpResponseTest() {

    override fun createResponseWithCode(statusCode: Int) =
            JsonHttpResponse(
                    codeValue = statusCode,
                    bodyAsText = null
            )

    @Test
    fun testBody() {
        assertThat(createJsonResponse(null).body, equalTo(JsonNull.INSTANCE as JsonElement))
        assertThat(createJsonResponse("").body, equalTo(JsonNull.INSTANCE as JsonElement))
        assertThat(createJsonResponse(
                "{ \"key\": \"value\" }").body,
                equalTo(jsonObject("key" to "value") as JsonElement)
        )
    }

    private fun createJsonResponse(body: String?) =
            JsonHttpResponse(
                    codeValue = 200,
                    bodyAsText = body
            )
}