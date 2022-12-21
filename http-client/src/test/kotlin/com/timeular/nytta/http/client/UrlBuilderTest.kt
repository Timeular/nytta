package com.timeular.nytta.http.client

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.timeular.nytta.prova.hamkrest.containsExactly
import com.timeular.nytta.prova.hamkrest.containsInAnyOrder
import org.junit.jupiter.api.Test

class UrlBuilderTest {

    @Test
    fun testBuildUrlUrlOnly() {
        val result = UrlBuilder.newBuilder()
            .url("http://n1url/")
            .build()

        assertThat(result, equalTo("http://n1url"))
    }

    @Test
    fun testBuildUrlParameterOnly() {
        val result = UrlBuilder.newBuilder()
            .addUrlParameter("key1", "value1")
            .addUrlParameter("key2", "value2")
            .addUrlParameter("key3", "value 3")
            .addUrlParameter("key4", "value+4")
            .build()

        assertThat(result, equalTo("?key1=value1&key2=value2&key3=value+3&key4=value%2B4"))
    }

    @Test
    fun testBuildUrl() {
        val result = UrlBuilder.newBuilder()
            .url("http://www.someurl.com/")
            .addUrlParameter("key1", "value1")
            .addUrlParameter("key2", "value2")
            .addUrlParameter("key3", "value 3")
            .addUrlParameter("key4", "value+4")
            .build()

        assertThat(result, equalTo("http://www.someurl.com?key1=value1&key2=value2&key3=value+3&key4=value%2B4"))
    }

    @Test
    fun testBuildUrlWithExistingParameters() {
        val result = UrlBuilder.newBuilder()
            .url("http://www.someurl.com?key0=value0")
            .addUrlParameter("key1", "value1")
            .build()

        assertThat(result, equalTo("http://www.someurl.com?key0=value0&key1=value1"))
    }

    @Test
    fun testBuilderUrl() {
        val result = UrlBuilder.newBuilder()
            .url("http://www.someurl.com?key1=value1&key2=value2&key3=value+3&key4=value%2B4#best-thing")
            .addUrlParameter("key4", "value 4")
            .addUrlParameter("key5", "value+5")
            .build()

        assertThat(
            result,
            equalTo("http://www.someurl.com?key1=value1&key2=value2&key3=value+3&key4=value%2B4&key4=value+4&key5=value%2B5#best-thing")
        )
    }

    @Test
    fun testOmitDuplicates() {
        val builder = UrlBuilder.newBuilder()
            .url("http://www.someurl.com?key1=value1")
            .addUrlParameter("key1", "value2")
            .addUrlParameter("key1", "value3")

        assertThat(
            builder.build(),
            equalTo("http://www.someurl.com?key1=value1&key1=value2&key1=value3")
        )

        assertThat(
            builder.omitDuplicatedParameters(true).build(),
            equalTo("http://www.someurl.com?key1=value1")
        )
    }

    @Test
    fun testParseQueryParameters() {
        val res =
            UrlBuilder.parseQueryParams("http://www.someurl.com?key1=value1&key2=value2&key3=value+3&key4=value%2B4&key4=value+4&key5=value%2B5#best-thing")

        assertThat(res["key1"] ?: emptyList(), containsExactly("value1"))
        assertThat(res["key2"] ?: emptyList(), containsExactly("value2"))
        assertThat(res["key3"] ?: emptyList(), containsExactly("value 3"))
        assertThat(res["key4"] ?: emptyList(), containsInAnyOrder("value 4", "value+4"))
        assertThat(res["key5"] ?: emptyList(), containsExactly("value+5"))
    }
}