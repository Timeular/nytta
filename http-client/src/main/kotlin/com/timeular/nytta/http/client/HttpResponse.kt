package com.timeular.nytta.http.client

import okhttp3.Headers

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

    private fun is2xxSuccessful() = code in (200..299)

    fun wasResponseError() = !is2xxSuccessful()

    fun isClientError() = code in (400..499)

    fun isServerError() = code in (500..599)
}