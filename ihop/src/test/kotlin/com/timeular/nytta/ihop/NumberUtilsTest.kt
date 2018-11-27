package com.timeular.nytta.ihop

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class NumberUtilsTest {

    @Test
    fun testToOrdinalString() {
        assert.that(0.toOrdinalString(), equalTo("0th"))
        assert.that(1.toOrdinalString(), equalTo("1st"))
        assert.that(2.toOrdinalString(), equalTo("2nd"))
        assert.that(3.toOrdinalString(), equalTo("3rd"))
        assert.that(4.toOrdinalString(), equalTo("4th"))
        assert.that(5.toOrdinalString(), equalTo("5th"))

        assert.that(11.toOrdinalString(), equalTo("11th"))
        assert.that(12.toOrdinalString(), equalTo("12th"))
        assert.that(13.toOrdinalString(), equalTo("13th"))

        assert.that(20.toOrdinalString(), equalTo("20th"))
        assert.that(21.toOrdinalString(), equalTo("21st"))
        assert.that(22.toOrdinalString(), equalTo("22nd"))
        assert.that(23.toOrdinalString(), equalTo("23rd"))
        assert.that(24.toOrdinalString(), equalTo("24th"))

        assert.that(40.toOrdinalString(), equalTo("40th"))
        assert.that(41.toOrdinalString(), equalTo("41st"))
        assert.that(42.toOrdinalString(), equalTo("42nd"))
        assert.that(43.toOrdinalString(), equalTo("43rd"))
        assert.that(44.toOrdinalString(), equalTo("44th"))

        assert.that(100.toOrdinalString(), equalTo("100th"))
        assert.that(101.toOrdinalString(), equalTo("101st"))
        assert.that(102.toOrdinalString(), equalTo("102nd"))
        assert.that(103.toOrdinalString(), equalTo("103rd"))
        assert.that(104.toOrdinalString(), equalTo("104th"))

        assert.that(110.toOrdinalString(), equalTo("110th"))
        assert.that(111.toOrdinalString(), equalTo("111th"))
        assert.that(112.toOrdinalString(), equalTo("112th"))
        assert.that(113.toOrdinalString(), equalTo("113th"))
    }
}