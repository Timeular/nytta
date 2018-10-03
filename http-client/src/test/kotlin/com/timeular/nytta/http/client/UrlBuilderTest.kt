package com.timeular.nytta.http.client

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class UrlBuilderTest {

    @Test
    fun testBuildUrlUrlOnly() {
        val result = UrlBuilder.newBuilder()
                .url("http://n1url/")
                .build()

        assert.that(result, equalTo("http://n1url"))
    }

    @Test
    fun testBuildUrlParameterOnly() {
        val result = UrlBuilder.newBuilder()
                .addUrlParameter("key1", "value1")
                .addUrlParameter("key2", "value2")
                .addUrlParameter("key3", "value 3")
                .addUrlParameter("key4", "value+4")
                .build()

        assert.that(result, equalTo("?key1=value1&key2=value2&key3=value+3&key4=value%2B4"))
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

        assert.that(result, equalTo("http://www.someurl.com?key1=value1&key2=value2&key3=value+3&key4=value%2B4"))
    }
}