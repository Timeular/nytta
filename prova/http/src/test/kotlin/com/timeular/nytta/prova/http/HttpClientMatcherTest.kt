package com.timeular.nytta.prova.http

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.timeular.nytta.http.client.HttpResponse
import com.timeular.nytta.http.client.TextHttpResponse
import org.junit.jupiter.api.Test

class HttpClientMatcherTest {

    @Test
    fun testHttpSuccessful() {
        for (i in 200..299) {
            testWithStatusCode(i, httpSuccessful)
        }
        for (i in 100..199) {
            testWithStatusCode(i, !httpSuccessful)
        }
        for (i in 300..999) {
            testWithStatusCode(i, !httpSuccessful)
        }
    }

    @Test
    fun testHttpClientError() {
        for (i in 400..499) {
            testWithStatusCode(i, httpClientError)
        }
        for (i in 100..399) {
            testWithStatusCode(i, !httpClientError)
        }
        for (i in 500..999) {
            testWithStatusCode(i, !httpClientError)
        }
    }

    @Test
    fun testHttpServerError() {
        for (i in 500..599) {
            testWithStatusCode(i, httpServerError)
        }
        for (i in 100..499) {
            testWithStatusCode(i, !httpServerError)
        }
        for (i in 600..999) {
            testWithStatusCode(i, !httpServerError)
        }
    }

    @Test
    fun test1xxCodes() {
        testWithStatusCode(100, httpContinue)
        testWithStatusCode(101, httpSwitchingProtocols)
    }

    @Test
    fun test2xxCodes() {
        testWithStatusCode(200, httpOk)
        testWithStatusCode(201, httpCreated)
        testWithStatusCode(204, httpNoContent)
    }

    @Test
    fun test3xxCodes() {
        testWithStatusCode(301, httpMovedPermanently)
        testWithStatusCode(302, httpMovedTemporarily)
        testWithStatusCode(302, httpFound)
    }

    @Test
    fun test4xxCodes() {
        testWithStatusCode(400, httpBadRequest)
        testWithStatusCode(401, httpUnauthorized)
        testWithStatusCode(403, httpForbidden)
        testWithStatusCode(404, httpNotFound)
        testWithStatusCode(405, httpMethodNotAllowed)
        testWithStatusCode(409, httpConflict)
    }

    @Test
    fun test5xxCodes() {
        testWithStatusCode(500, httpInternalServerError)
        testWithStatusCode(502, httpBadGateway)
        testWithStatusCode(503, httpServiceUnavailable)
        testWithStatusCode(504, httpGatewayTimeout)
    }

    private fun testWithStatusCode(status: Int, matcher: Matcher<HttpResponse<*>>) {
        assertThat(createHttpResponse(status), matcher)
    }

    private fun createHttpResponse(status: Int) = TextHttpResponse(
            codeValue = status,
            bodyAsText = null
    )
}