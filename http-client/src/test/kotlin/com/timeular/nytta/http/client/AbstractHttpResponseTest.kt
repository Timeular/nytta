package com.timeular.nytta.http.client

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

abstract class AbstractHttpResponseTest {

    abstract fun createResponseWithCode(statusCode: Int): HttpResponse<Any>

    @Test
    fun testIsClientError() {
        for (statusCode in 100..599) {
            val resp = createResponseWithCode(statusCode)

            when (statusCode) {
                in 400..499 -> assertThat(resp.isClientError(), equalTo(true))
                else -> assertThat(resp.isClientError(), equalTo(false))
            }
        }
    }

    @Test
    fun testIsServerError() {
        for (statusCode in 100..599) {
            val resp = createResponseWithCode(statusCode)

            when (statusCode) {
                in 500..599 -> assertThat(resp.isServerError(), equalTo(true))
                else -> assertThat(resp.isServerError(), equalTo(false))
            }
        }
    }

    @Test
    fun testWasSuccessful() {
        for (statusCode in 100..599) {
            val resp = createResponseWithCode(statusCode)

            when (statusCode) {
                in 200..299 -> assertThat(resp.wasSuccessful(), equalTo(true))
                else -> assertThat(resp.wasSuccessful(), equalTo(false))
            }
        }
    }

    @Test
    fun testWasError() {
        for (statusCode in 100..599) {
            val resp = createResponseWithCode(statusCode)

            when (statusCode) {
                in 200..299 -> assertThat(resp.wasResponseError(), equalTo(false))
                else -> assertThat(resp.wasResponseError(), equalTo(true))
            }
        }
    }

}