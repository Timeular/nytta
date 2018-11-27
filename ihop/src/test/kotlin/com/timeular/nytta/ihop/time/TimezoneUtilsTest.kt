package com.timeular.nytta.ihop.time

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*


class TimezoneUtilsTest {

    private val dateInWinter = LocalDate.of(2018, 1, 10).toDate()
    private val dateInSummer = LocalDate.of(2018, 8, 10).toDate()

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
        assert.that(toUTCOffsetString("UTC", dateInWinter), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("UTC", dateInSummer), equalTo("UTC+00:00"))

        assert.that(toUTCOffsetString("UTC+00:00", dateInWinter), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("UTC+00:00", dateInSummer), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("UTC+01:00", dateInWinter), equalTo("UTC+01:00"))
        assert.that(toUTCOffsetString("UTC+01:00", dateInSummer), equalTo("UTC+01:00"))
        assert.that(toUTCOffsetString("UTC-14:00", dateInWinter), equalTo("UTC-14:00"))
        assert.that(toUTCOffsetString("UTC-14:00", dateInSummer), equalTo("UTC-14:00"))
        assert.that(toUTCOffsetString("BLARRRGDSdfdsga"), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("utc", dateInWinter), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("uTc", dateInSummer), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("GMT", dateInWinter), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("GMT", dateInSummer), equalTo("UTC+00:00"))
        assert.that(toUTCOffsetString("CET", dateInWinter), equalTo("UTC+01:00"))
        assert.that(toUTCOffsetString("CET", dateInSummer), equalTo("UTC+02:00"))
        assert.that(toUTCOffsetString("PST", dateInWinter), equalTo("UTC-08:00"))
        assert.that(toUTCOffsetString("PST", dateInSummer), equalTo("UTC-07:00"))
        assert.that(toUTCOffsetString("JST", dateInWinter), equalTo("UTC+09:00"))
        assert.that(toUTCOffsetString("JST", dateInSummer), equalTo("UTC+09:00"))
        assert.that(toUTCOffsetString("CST", dateInWinter), equalTo("UTC-06:00"))
        assert.that(toUTCOffsetString("CST", dateInSummer), equalTo("UTC-05:00"))
        assert.that(toUTCOffsetString("Asia/Tehran", dateInWinter), equalTo("UTC+03:30"))
        assert.that(toUTCOffsetString("Asia/Tehran", dateInSummer), equalTo("UTC+04:30"))
        assert.that(toUTCOffsetString("Canada/Newfoundland", dateInWinter), equalTo("UTC-03:30"))
        assert.that(toUTCOffsetString("Canada/Newfoundland", dateInSummer), equalTo("UTC-02:30"))
        assert.that(toUTCOffsetString("Etc/GMT-14", dateInWinter), equalTo("UTC+14:00"))
        assert.that(toUTCOffsetString("Etc/GMT-14", dateInSummer), equalTo("UTC+14:00"))
        assert.that(toUTCOffsetString("Pacific/Midway", dateInWinter), equalTo("UTC-11:00"))
        assert.that(toUTCOffsetString("Pacific/Midway", dateInSummer), equalTo("UTC-11:00"))
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

    private fun LocalDate.toDate(): Date =
            Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}