package com.timeular.nytta.ihop

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class NumberUtilsTest {

    @Test
    fun testToOrdinalString() {
        assertThat(0.toOrdinalString(), equalTo("0th"))
        assertThat(1.toOrdinalString(), equalTo("1st"))
        assertThat(2.toOrdinalString(), equalTo("2nd"))
        assertThat(3.toOrdinalString(), equalTo("3rd"))
        assertThat(4.toOrdinalString(), equalTo("4th"))
        assertThat(5.toOrdinalString(), equalTo("5th"))

        assertThat(11.toOrdinalString(), equalTo("11th"))
        assertThat(12.toOrdinalString(), equalTo("12th"))
        assertThat(13.toOrdinalString(), equalTo("13th"))

        assertThat(20.toOrdinalString(), equalTo("20th"))
        assertThat(21.toOrdinalString(), equalTo("21st"))
        assertThat(22.toOrdinalString(), equalTo("22nd"))
        assertThat(23.toOrdinalString(), equalTo("23rd"))
        assertThat(24.toOrdinalString(), equalTo("24th"))

        assertThat(40.toOrdinalString(), equalTo("40th"))
        assertThat(41.toOrdinalString(), equalTo("41st"))
        assertThat(42.toOrdinalString(), equalTo("42nd"))
        assertThat(43.toOrdinalString(), equalTo("43rd"))
        assertThat(44.toOrdinalString(), equalTo("44th"))

        assertThat(100.toOrdinalString(), equalTo("100th"))
        assertThat(101.toOrdinalString(), equalTo("101st"))
        assertThat(102.toOrdinalString(), equalTo("102nd"))
        assertThat(103.toOrdinalString(), equalTo("103rd"))
        assertThat(104.toOrdinalString(), equalTo("104th"))

        assertThat(110.toOrdinalString(), equalTo("110th"))
        assertThat(111.toOrdinalString(), equalTo("111th"))
        assertThat(112.toOrdinalString(), equalTo("112th"))
        assertThat(113.toOrdinalString(), equalTo("113th"))
    }
}