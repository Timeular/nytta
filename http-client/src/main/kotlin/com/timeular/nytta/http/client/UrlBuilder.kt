package com.timeular.nytta.http.client

import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * a builder which helps to build correct urls with url-encoded parameters
 */
class UrlBuilder private constructor() {

    private var url = ""
    private var fragment: String? = null
    private val queryParameter = LinkedHashMap<String, List<String>>()
    private var urlParameter = LinkedHashMap<String, List<String>>()
    private var omitDuplicatedParameters = false

    companion object {
        fun newBuilder(): UrlBuilder = UrlBuilder()
    }

    fun url(url: String): UrlBuilder {
        val uri = URI.create(url)
        this.url = url

        if (uri.fragment != null) {
            this.url = this.url.replace("#${uri.fragment}", "")
        }

        if (uri.rawQuery != null) {
            this.url = this.url.replace("?${uri.rawQuery}", "")
        }

        this.fragment = uri.rawFragment

        uri.rawQuery?.run {
            if (this.isNotBlank()) {
                this.split("&").forEach { param ->
                    val split = param.split("=", limit = 2)
                    if (split.size < 2) {
                        throw IllegalArgumentException("Invalid query parameter found: $param")
                    }
                    val key = split[0]
                    val list = queryParameter[key]?.toMutableList() ?: mutableListOf()
                    list.add(URLDecoder.decode(split[1], Charsets.UTF_8.name()))
                    queryParameter[key] = list
                }
            }
        }

        return this
    }

    fun addUrlParameter(key: String, value: String): UrlBuilder {
        if (key.isNullOrEmpty()) {
            throw IllegalArgumentException("The key is not allowed to be empty")
        }

        if (value.isNullOrEmpty()) {
            throw IllegalArgumentException("The value is not allowed to be empty")
        }

        val list = urlParameter[key]?.toMutableList() ?: mutableListOf()
        list.add(value)
        urlParameter[key] = list
        return this
    }

    fun omitDuplicatedParameters(value: Boolean): UrlBuilder {
        omitDuplicatedParameters = value
        return this
    }

    fun build(): String {
        var resultUrl = url

        if (resultUrl.endsWith("/")) {
            resultUrl = resultUrl.substring(0, resultUrl.length - 1)
        }

        val queryParams = createQueryParams(queryParameter)
        val queryParameterKeys = queryParameter.keys

        val urlParams = createQueryParams(
            if (omitDuplicatedParameters) {
                urlParameter.filterNot { (key, _) -> queryParameterKeys.contains(key) }
            } else {
                urlParameter
            }
        )

        val params = queryParams?.let { q ->
            "?$q${urlParams?.let { "&$it" } ?: ""}"
        } ?: urlParams?.let { "?$it" } ?: ""

        return "$resultUrl$params${fragment?.let { "#$it" } ?: ""}"
    }

    private fun createQueryParams(params: Map<String, List<String>>): String? =
        if (params.isNotEmpty()) {
            params.toList().joinToString("&") { (key, value) ->
                if (omitDuplicatedParameters) {
                    value.firstOrNull()?.let {
                        val encodedValue = URLEncoder.encode(it, Charsets.UTF_8.name())
                        "$key=$encodedValue"
                    } ?: ""
                } else {
                    value.joinToString("&") { strValue ->
                        val encodedValue = URLEncoder.encode(strValue, Charsets.UTF_8.name())
                        "$key=$encodedValue"
                    }
                }

            }
        } else {
            null
        }
}
