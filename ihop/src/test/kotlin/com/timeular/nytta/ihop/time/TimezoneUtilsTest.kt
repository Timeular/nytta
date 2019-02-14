package com.timeular.nytta.ihop.time

import com.natpryce.hamkrest.assertion.assertThat
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
        assertThat(zoneOffsetFromTimezone("UTC+00:00"), equalTo(ZoneOffset.UTC))
        assertThat(zoneOffsetFromTimezone("UTC+02:00"), equalTo(ZoneOffset.ofHours(2)))
    }

    @Test
    fun testConversion() {
        assertThat(toUTCOffsetString("UTC", dateInWinter), equalTo("UTC+00:00"))
        assertThat(toUTCOffsetString("UTC", dateInSummer), equalTo("UTC+00:00"))

        assertThat(toUTCOffsetString("UTC+00:00", dateInWinter), equalTo("UTC+00:00"))
        assertThat(toUTCOffsetString("UTC+00:00", dateInSummer), equalTo("UTC+00:00"))
        assertThat(toUTCOffsetString("UTC+01:00", dateInWinter), equalTo("UTC+01:00"))
        assertThat(toUTCOffsetString("UTC+01:00", dateInSummer), equalTo("UTC+01:00"))
        assertThat(toUTCOffsetString("UTC-14:00", dateInWinter), equalTo("UTC-14:00"))
        assertThat(toUTCOffsetString("UTC-14:00", dateInSummer), equalTo("UTC-14:00"))
        assertThat(toUTCOffsetString("BLARRRGDSdfdsga"), equalTo("UTC+00:00"))
        assertThat(toUTCOffsetString("utc", dateInWinter), equalTo("UTC+00:00"))
        assertThat(toUTCOffsetString("uTc", dateInSummer), equalTo("UTC+00:00"))
        assertThat(toUTCOffsetString("GMT", dateInWinter), equalTo("UTC+00:00"))
        assertThat(toUTCOffsetString("GMT", dateInSummer), equalTo("UTC+00:00"))
        assertThat(toUTCOffsetString("CET", dateInWinter), equalTo("UTC+01:00"))
        assertThat(toUTCOffsetString("CET", dateInSummer), equalTo("UTC+02:00"))
        assertThat(toUTCOffsetString("PST", dateInWinter), equalTo("UTC-08:00"))
        assertThat(toUTCOffsetString("PST", dateInSummer), equalTo("UTC-07:00"))
        assertThat(toUTCOffsetString("JST", dateInWinter), equalTo("UTC+09:00"))
        assertThat(toUTCOffsetString("JST", dateInSummer), equalTo("UTC+09:00"))
        assertThat(toUTCOffsetString("CST", dateInWinter), equalTo("UTC-06:00"))
        assertThat(toUTCOffsetString("CST", dateInSummer), equalTo("UTC-05:00"))
        assertThat(toUTCOffsetString("Asia/Tehran", dateInWinter), equalTo("UTC+03:30"))
        assertThat(toUTCOffsetString("Asia/Tehran", dateInSummer), equalTo("UTC+04:30"))
        assertThat(toUTCOffsetString("Canada/Newfoundland", dateInWinter), equalTo("UTC-03:30"))
        assertThat(toUTCOffsetString("Canada/Newfoundland", dateInSummer), equalTo("UTC-02:30"))
        assertThat(toUTCOffsetString("Etc/GMT-14", dateInWinter), equalTo("UTC+14:00"))
        assertThat(toUTCOffsetString("Etc/GMT-14", dateInSummer), equalTo("UTC+14:00"))
        assertThat(toUTCOffsetString("Pacific/Midway", dateInWinter), equalTo("UTC-11:00"))
        assertThat(toUTCOffsetString("Pacific/Midway", dateInSummer), equalTo("UTC-11:00"))
        assertThat(toUTCOffsetString("pacific/midway"), equalTo("UTC-11:00"))

        assertThat(toUTCOffsetString(ZoneOffset.UTC), equalTo("UTC+00:00"))
    }

    @Test
    fun testIsUTCOffsetString() {
        assertThat(isUTCOffsetString("UTC"), equalTo(false))
        assertThat(isUTCOffsetString("UTC-01:00"), equalTo(true))
        assertThat(isUTCOffsetString("UTC-00:00"), equalTo(true))
        assertThat(isUTCOffsetString("UTC+00:00"), equalTo(true))
        assertThat(isUTCOffsetString("UTC+02:00"), equalTo(true))
        assertThat(isUTCOffsetString("UTC+15:00"), equalTo(false))
        assertThat(isUTCOffsetString("UTC+14:00"), equalTo(true))
        assertThat(isUTCOffsetString("UTC-20:00"), equalTo(false))
        assertThat(isUTCOffsetString("UTC-03:22"), equalTo(false))
        assertThat(isUTCOffsetString("UTC-03:30"), equalTo(true))
        assertThat(isUTCOffsetString("UTC-02:45"), equalTo(true))
        assertThat(isUTCOffsetString("UTC-02:15"), equalTo(true))
        assertThat(isUTCOffsetString("UTC-1"), equalTo(false))


        assertThat("UTC".isUTCOffset(), equalTo(false))
        assertThat("UTC-00:00".isUTCOffset(), equalTo(true))
        val nullString: String? = null
        assertThat(nullString.isUTCOffset(), equalTo(false))
    }

    private fun LocalDate.toDate(): Date =
            Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}