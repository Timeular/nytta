package com.timeular.nytta.http.client

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.throws
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import okhttp3.Headers
import org.junit.jupiter.api.Test
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

internal class RetryableHttpClientTest {

    private val httpClient = spy<HttpClient> {
        on { request(any(), any(), eq(null), any()) } doReturn JsonHttpResponse(
                codeValue = 503,
                headers = Headers.of(emptyMap()),
                bodyAsText = null
        )

        on { requestWithTextResponse(any(), any(), eq(null), any()) } doReturn TextHttpResponse(
                codeValue = 503,
                headers = Headers.of(emptyMap()),
                bodyAsText = null
        )
    }

    private val retryableHttpClient = RetryableHttpClient(
            retryTemplate = createRetryTemplate(),
            httpClientDelegate = httpClient
    )

    @Test
    fun testRetryRequest() {
        assert.that(
                {
                    retryableHttpClient.request(
                            httpMethod = HttpMethod.GET,
                            url = "http://some/url"
                    )
                },
                throws<HttpClientException>()
        )

        verify(httpClient, times(3)).request(any(), any(), eq(null), any())
    }

    @Test
    fun testRetryRequestWithTextResponse(){
        assert.that(
                {
                    retryableHttpClient.requestWithTextResponse(
                            httpMethod = HttpMethod.GET,
                            url = "http://some/url"
                    )
                },
                throws<HttpClientException>()
        )

        verify(httpClient, times(3)).requestWithTextResponse(any(), any(), eq(null), any())
    }

    private fun createRetryTemplate(): RetryTemplate {
        val backoffPolicy = FixedBackOffPolicy()
        backoffPolicy.backOffPeriod = 100

        val retryTemplate = RetryTemplate()
        retryTemplate.setRetryPolicy(SimpleRetryPolicy(3))
        retryTemplate.setBackOffPolicy(backoffPolicy)

        return retryTemplate
    }
}