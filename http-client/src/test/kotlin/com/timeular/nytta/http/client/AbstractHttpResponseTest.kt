package com.timeular.nytta.http.client

import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.assertion.assert
import org.junit.jupiter.api.Test

abstract class AbstractHttpResponseTest {

    abstract fun createResponseWithCode(statusCode: Int): HttpResponse<Any>

    @Test
    fun testIsClientError() {
        for (statusCode in 100..599) {
            val resp = createResponseWithCode(statusCode)

            when(statusCode){
                in 400..499 -> assert.that(resp.isClientError(), equalTo(true))
                else -> assert.that(resp.isClientError(), equalTo(false))
            }
        }
    }

    @Test
    fun testIsServerError() {
        for (statusCode in 100..599) {
            val resp = createResponseWithCode(statusCode)

            when(statusCode){
                in 500..599 -> assert.that(resp.isServerError(), equalTo(true))
                else -> assert.that(resp.isServerError(), equalTo(false))
            }
        }
    }

    @Test
    fun testWasSuccessful() {
        for (statusCode in 100..599) {
            val resp = createResponseWithCode(statusCode)

            when(statusCode){
                in 200..299 -> assert.that(resp.wasSuccessful(), equalTo(true))
                else -> assert.that(resp.wasSuccessful(), equalTo(false))
            }
        }
    }

    @Test
    fun testWasError(){
        for (statusCode in 100..599) {
            val resp = createResponseWithCode(statusCode)

            when(statusCode){
                in 200..299 -> assert.that(resp.wasResponseError(), equalTo(false))
                else ->  assert.that(resp.wasResponseError(), equalTo(true))
            }
        }
    }

}