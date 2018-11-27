package com.timeular.nytta.ihop.time

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.ZoneOffset


class TimezoneUtilsTest {

    @Test
    fun testZoneOffsetFromTimezoneWithUnknownTimezone() {
        Assertions.assertThrows(UnknownTimezoneException::class.java) { zoneOffsetFromTimezone("invalid") }
    }

    @Test
    fun testZoneOffsetFromTimezone() {
        assert.that(zoneOffsetFromTimezone("UTC+00:00"), equalTo(ZoneOffset.UTC))
        assert.that(zoneOffsetFromTimezone("UTC+02:00"), equalTo(ZoneOffset.ofHours(2)))
    }

    @Test
    fun testConversion() {
        assert.that(toUTCOffsetString("UTC"), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("UTC+00:00"), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("UTC+01:00"), equalTo("UTC+01:00"))
        assert.that(toUTCOffsetString("UTC-14:00"), equalTo("UTC-14:00"))
        assert.that(toUTCOffsetString("BLARRRGDSdfdsga"), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("utc"), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("uTc"), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("GMT"), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("CET"), equalTo("UTC+01:00"))
        assert.that(toUTCOffsetString("PST"), equalTo("UTC-08:00"))
        assert.that(toUTCOffsetString("JST"), equalTo("UTC+09:00"))
        assert.that(toUTCOffsetString("CST"), equalTo("UTC-06:00"))
        assert.that(toUTCOffsetString("Asia/Tehran"), equalTo("UTC+03:30"))
        assert.that(toUTCOffsetString("Canada/Newfoundland"), equalTo("UTC-03:30"))
        assert.that(toUTCOffsetString("Etc/GMT-14"), equalTo("UTC+14:00"))
        assert.that(toUTCOffsetString("Pacific/Midway"), equalTo("UTC-11:00"))
        assert.that(toUTCOffsetString("pacific/midway"), equalTo("UTC-11:00"))

        assert.that(toUTCOffsetString(ZoneOffset.UTC), equalTo("UTC+00:00"))
    }

    @Test
    fun testIsUTCOffsetString() {
        assert.that(isUTCOffsetString("UTC"), equalTo(false))
        assert.that(isUTCOffsetString("UTC-01:00"), equalTo(true))
        assert.that(isUTCOffsetString("UTC-00:00"), equalTo(true))
        assert.that(isUTCOffsetString("UTC+00:00"), equalTo(true))
        assert.that(isUTCOffsetString("UTC+02:00"), equalTo(true))
        assert.that(isUTCOffsetString("UTC+15:00"), equalTo(false))
        assert.that(isUTCOffsetString("UTC+14:00"), equalTo(true))
        assert.that(isUTCOffsetString("UTC-20:00"), equalTo(false))
        assert.that(isUTCOffsetString("UTC-03:22"), equalTo(false))
        assert.that(isUTCOffsetString("UTC-03:30"), equalTo(true))
        assert.that(isUTCOffsetString("UTC-02:45"), equalTo(true))
        assert.that(isUTCOffsetString("UTC-02:15"), equalTo(true))
        assert.that(isUTCOffsetString("UTC-1"), equalTo(false))


        assert.that("UTC".isUTCOffset(), equalTo(false))
        assert.that("UTC-00:00".isUTCOffset(), equalTo(true))
        val nullString: String? = null
        assert.that(nullString.isUTCOffset(), equalTo(false))
    }
}