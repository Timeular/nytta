package com.timeular.nytta.ihop

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class NumberFormatUtilsTest {

    @Test
    fun testFormatDouble() {
        assertThat(NumberFormatUtils.formatDouble(0.15), equalTo("0.2"))
        assertThat(NumberFormatUtils.formatDouble(1.5453), equalTo("1.5"))
        assertThat(NumberFormatUtils.formatDouble(1.0), equalTo("1"))
        assertThat(NumberFormatUtils.formatDouble(0.01), equalTo("0"))
    }

    @Test
    fun testFormatBigDecimal() {
        assertThat(NumberFormatUtils.formatBigDecimal(BigDecimal.valueOf(0.0)), equalTo("0"))
        assertThat(NumberFormatUtils.formatBigDecimal(BigDecimal.valueOf(1.2345)), equalTo("1.2"))
        assertThat(NumberFormatUtils.formatBigDecimal(BigDecimal.valueOf(5.55)), equalTo("5.6"))
        assertThat(NumberFormatUtils.formatBigDecimal(BigDecimal.valueOf(4.01)), equalTo("4"))
    }
}