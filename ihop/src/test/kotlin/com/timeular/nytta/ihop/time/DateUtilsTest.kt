package com.timeular.nytta.ihop.time

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class DateUtilsTest {

    @Test
    fun testDateTimeFormatter() {
        val date = ZonedDateTime.parse("2018-05-18T12:40:30.304+01:00")

        assertThat(dateTimeFormatter(false).format(date), equalTo("2018-05-18T12:40:30.304"))
        assertThat(dateTimeFormatter(true).format(date), equalTo("2018-05-18T12:40:30.304+0100"))
    }

    @Test
    fun testConvertMillisToHours() {
        assertThat(convertMillisToHours(18000000L), equalTo(5.0))
        assertThat(convertMillisToHours(0L), equalTo(0.0))
    }

    @Test
    fun testFormatDayMonthToString() {
        assertThat(formatDayMonthToString("2018-05-28T00:00:00.000+0000".toZonedDateTime()), equalTo("May 28th"))
        assertThat(formatDayMonthToString("2018-06-04T23:59:59.999+0000".toZonedDateTime()), equalTo("June 4th"))
        assertThat(formatDayMonthToString("2018-06-03T23:59:59.999+0000".toZonedDateTime()), equalTo("June 3rd"))
        assertThat(formatDayMonthToString("2018-06-02T23:59:59.999+0000".toZonedDateTime()), equalTo("June 2nd"))
        assertThat(formatDayMonthToString("2018-06-01T23:59:59.999+0000".toZonedDateTime()), equalTo("June 1st"))
    }

    @Test
    fun testConvertHoursToMillis() {
        assertThat(convertHoursToMillis(5.0), equalTo(18000000L))
        assertThat(convertHoursToMillis(0.0), equalTo(0L))
    }

    @Test
    fun testFirstDayOfWeekOfYear() {
        assertThat(firstDayOfWeekOfYear(13, 2018, UTC), equalTo(ZonedDateTime.parse("2018-03-26T00:00Z")))
        assertThat(firstDayOfWeekOfYear(1, 2019, UTC), equalTo(ZonedDateTime.parse("2018-12-31T00:00Z")))
    }

    @Test
    fun testSearchPreviousMonday() {
        val thursday = ZonedDateTime.parse("2018-07-05T14:45:12.050Z")
        val monday = searchPreviousMonday(thursday)

        assertThat(dateTimeFormatter(true).format(monday), equalTo("2018-07-02T00:00:00.000+0000"))
    }
}