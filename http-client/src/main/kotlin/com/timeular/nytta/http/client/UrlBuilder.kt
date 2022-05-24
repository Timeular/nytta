package com.timeular.nytta.http.client

import java.net.URLEncoder

/**
 * a builder which helps to build correct urls with url-encoded parameters
 */
class UrlBuilder private constructor() {

    private var url = ""

    private var urlParameter = LinkedHashMap<String, String>()

    companion object {
        fun newBuilder(): UrlBuilder = UrlBuilder()
    }

    fun url(url: String): UrlBuilder {
        this.url = url
        return this
    }

    fun addUrlParameter(key: String, value: String): UrlBuilder {
        if (key.isNullOrEmpty()) {
            throw IllegalArgumentException("The key is not allowed to be empty")
        }

        if (value.isNullOrEmpty()) {
            throw IllegalArgumentException("The value is not allowed to be empty")
        }

        urlParameter[key] = value
        return this
    }

    fun build(): String {
        var resultUrl = url

        if (resultUrl.endsWith("/")) {
            resultUrl = resultUrl.substring(0, resultUrl.length - 1)
        }

        return if (urlParameter.isNotEmpty()) {
            val parameters = urlParameter.toList().joinToString("&") { (key, value) ->
                val encodedValue = URLEncoder.encode(value, Charsets.UTF_8.name())
                "$key=$encodedValue"
            }

            return "$resultUrl?$parameters"
        } else {
            resultUrl
        }
    }
}
