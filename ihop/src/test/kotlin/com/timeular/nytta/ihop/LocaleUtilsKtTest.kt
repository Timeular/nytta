package com.timeular.nytta.ihop

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.util.*

class LocaleUtilsKtTest {
    @Test
    fun testParseLocale() {
        assertThat(parseLocale("en"), equalTo(Locale.ENGLISH))
        assertThat(parseLocale("invalid"), absent())
        assertThat(parseLocale(" en-GB  "), equalTo(Locale.UK))
        assertThat(parseLocale("en_GB"), equalTo(Locale.UK))
        assertThat(parseLocale(null), absent())

        assertThat(parseLocale(null, Locale.ENGLISH), equalTo(Locale.ENGLISH))
        assertThat(parseLocale("  ", Locale.ENGLISH), equalTo(Locale.ENGLISH))
        assertThat(parseLocale("invalid", Locale.GERMAN), equalTo(Locale.GERMAN))
    }
}
