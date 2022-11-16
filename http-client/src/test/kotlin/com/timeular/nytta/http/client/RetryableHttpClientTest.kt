package com.timeular.nytta.http.client

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.throws
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

internal class RetryableHttpClientTest {

    private lateinit var httpClient: HttpClient

    private lateinit var retryableHttpClient: RetryableHttpClient

    @BeforeEach
    fun beforeEach() {
        httpClient = mockk()
        every { httpClient.request(any(), any(), isNull(), any()) } returns JsonHttpResponse(
            codeValue = 503,
            bodyAsText = null
        )
        every {
            httpClient.requestWithTextResponse(
                any(),
                any(),
                isNull(),
                any()
            )
        } returns TextHttpResponse(codeValue = 503, bodyAsText = null)

        retryableHttpClient = RetryableHttpClient(
            retryTemplate = createRetryTemplate(),
            httpClientDelegate = httpClient
        )
    }

    @Test
    fun testRetryRequest() {
        assertThat(
            {
                retryableHttpClient.request(
                    httpMethod = HttpMethod.GET,
                    url = "http://some/url"
                )
            },
            throws<HttpClientException>()
        )

        verify(exactly = 3) { httpClient.request(any(), any(), isNull(), any()) }
    }

    @Test
    fun testRetryRequestWithTextResponse() {
        assertThat(
            {
                retryableHttpClient.requestWithTextResponse(
                    httpMethod = HttpMethod.GET,
                    url = "http://some/url"
                )
            },
            throws<HttpClientException>()
        )

        verify(exactly = 3) { httpClient.requestWithTextResponse(any(), any(), isNull(), any()) }
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