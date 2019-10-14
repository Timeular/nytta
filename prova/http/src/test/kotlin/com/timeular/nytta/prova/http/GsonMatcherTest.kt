package com.timeular.nytta.prova.http

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.jupiter.api.Test

class GsonMatcherJsonArrayTest {

    @Test
    fun matchingIsEmpty() {
        assertThat(jsonArray(), isEmpty)
    }

    @Test
    fun mismatchIsEmpty() {
        assertThat("single primitive", jsonArray("test"), !isEmpty)
        assertThat("object", jsonArray(jsonObject("key" to "value")), !isEmpty)
        assertThat("single primitive + object", jsonArray(jsonObject("key" to "value"), "test"), !isEmpty)
    }
}