package com.timeular.nytta.ihop

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class CollectionUtilsKtTest {

    @Test
    fun testExecuteIfNotEmpty() {
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
}
