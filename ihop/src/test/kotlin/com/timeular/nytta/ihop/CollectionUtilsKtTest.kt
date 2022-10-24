package com.timeular.nytta.ihop

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class CollectionUtilsKtTest {

    @Test
    fun testListExecuteIfNotEmpty() {
        var executed = false
        emptyList<String>().executeIfNotEmpty {
            executed = true
        }
        assertThat(executed, equalTo(false))

        listOf(1).executeIfNotEmpty {
            executed = true
        }
        assertThat(executed, equalTo(true))
    }

    @Test
    fun testMapExecuteIfNotEmpty() {
        var executed = false
        emptyMap<String, String>().executeIfNotEmpty {
            executed = true
        }
        assertThat(executed, equalTo(false))

        mapOf(1 to 1).executeIfNotEmpty {
            executed = true
        }
        assertThat(executed, equalTo(true))
    }
}
