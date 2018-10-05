package com.timeular.nytta.http.client

import okhttp3.Headers

/**
 * Basic abstraction for a http response
 */
abstract class HttpResponse<out T>(
        val code: Int,
        val headers: Headers,
        private val bodyAsText: String?
) {

    abstract val body: T

    override fun toString(): String {
        return """
            |HTTP response:
            |- status code : $code
            |- body        : (below)
            |${bodyAsText ?: "(null)"}
            """.trimMargin()
    }

    /**
     * checks if the response code was 2xx
     */
    fun wasSuccessful() = code in (200..299)

    /**
     * checks if the response wasn't succesfull (2xx)
     */
    fun wasResponseError() = !wasSuccessful()

    /**
     * checks if the response code was 4xx
     */
    fun isClientError() = code in (400..499)

    /**
     * checks if the response code was 5xx
     */
    fun isServerError() = code in (500..599)
}