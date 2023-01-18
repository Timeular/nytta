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
    private var rfcCompliant = true

    companion object {
        @JvmStatic
        fun newBuilder(): UrlBuilder = UrlBuilder()

        @JvmStatic
        fun parseQueryParams(uri: String): Map<String, List<String>> =
            parseQueryParams(URI.create(uri))

        @JvmStatic
        fun parseQueryParams(uri: URI): Map<String, List<String>> =
            uri.rawQuery?.let { rawQuery ->
                if (rawQuery.isNotBlank()) {
                    val map = LinkedHashMap<String, List<String>>()
                    rawQuery.split("&").forEach { param ->
                        val split = param.split("=", limit = 2)
                        if (split.size < 2) {
                            throw IllegalArgumentException("Invalid query parameter found: $param")
                        }
                        val key = split[0]
                        val list = map[key]?.toMutableList() ?: mutableListOf()
                        list.add(URLDecoder.decode(split[1], Charsets.UTF_8.name()))
                        map[key] = list
                    }
                    map
                } else {
                    emptyMap()
                }
            } ?: emptyMap()

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

        val params = parseQueryParams(uri)
        if (params.isNotEmpty()) {
            queryParameter.putAll(params)
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

    fun disableRfcCompliance(): UrlBuilder {
        rfcCompliant = false
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

        return if (rfcCompliant) {
            "$resultUrl$params${fragment?.let { "#$it" } ?: ""}"
        } else {
            "$resultUrl${fragment?.let { "#$it" } ?: ""}$params"
        }
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
