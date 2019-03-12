package com.timeular.nytta.http.client

import com.google.common.base.Joiner
import com.google.common.base.Preconditions.checkArgument
import com.google.common.base.Strings.isNullOrEmpty
import java.net.URLEncoder
import java.util.*

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
        checkArgument(!isNullOrEmpty(key), "The key is not allowed to be empty")
        checkArgument(!isNullOrEmpty(value), "The value is not allowed to be empty")
        urlParameter[key] = value
        return this
    }

    fun build(): String {
        var resultUrl = url

        if (resultUrl.endsWith("/")) {
            resultUrl = resultUrl.substring(0, resultUrl.length - 1)
        }

        return if (urlParameter.isNotEmpty()) {
            val encodedParams = urlParameter.mapValues { URLEncoder.encode(it.value, Charsets.UTF_8.name()) }

            val parameters = Joiner.on("&")
                    .withKeyValueSeparator("=")
                    .join(encodedParams)

            return "$resultUrl?$parameters"
        } else {
            resultUrl
        }
    }
}
