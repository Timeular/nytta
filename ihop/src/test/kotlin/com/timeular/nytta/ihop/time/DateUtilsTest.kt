package com.timeular.nytta.ihop.time

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class DateUtilsTest {

    @Test
    fun testDateTimeFormatter() {
        val date = ZonedDateTime.parse("2018-05-18T12:40:30.304+01:00")

        assert.that(dateTimeFormatter(false).format(date), equalTo("2018-05-18T12:40:30.304"))
        assert.that(dateTimeFormatter(true).format(date), equalTo("2018-05-18T12:40:30.304+0100"))
    }

    @Test
    fun testConvertMillisToHours() {
        assert.that(convertMillisToHours(18000000L), equalTo(5.0))
        assert.that(convertMillisToHours(0L), equalTo(0.0))
    }

    @Test
    fun testFormatDayMonthToString() {
        assert.that(formatDayMonthToString("2018-05-28T00:00:00.000+0000".toZonedDateTime()), equalTo("May 28th"))
        assert.that(formatDayMonthToString("2018-06-04T23:59:59.999+0000".toZonedDateTime()), equalTo("June 4th"))
        assert.that(formatDayMonthToString("2018-06-03T23:59:59.999+0000".toZonedDateTime()), equalTo("June 3rd"))
        assert.that(formatDayMonthToString("2018-06-02T23:59:59.999+0000".toZonedDateTime()), equalTo("June 2nd"))
        assert.that(formatDayMonthToString("2018-06-01T23:59:59.999+0000".toZonedDateTime()), equalTo("June 1st"))
    }

    @Test
    fun testConvertHoursToMillis() {
        assert.that(convertHoursToMillis(5.0), equalTo(18000000L))
        assert.that(convertHoursToMillis(0.0), equalTo(0L))
    }

    @Test
    fun testFirstDayOfWeekOfYear() {
        assert.that(firstDayOfWeekOfYear(13, 2018, UTC), equalTo(ZonedDateTime.parse("2018-03-26T00:00Z")))
        assert.that(firstDayOfWeekOfYear(1, 2019, UTC), equalTo(ZonedDateTime.parse("2018-12-31T00:00Z")))
    }

    @Test
    fun testSearchPreviousMonday() {
        val thursday = ZonedDateTime.parse("2018-07-05T14:45:12.050Z")
        val monday = searchPreviousMonday(thursday)

        assert.that(dateTimeFormatter(true).format(monday), equalTo("2018-07-02T00:00:00.000+0000"))
    }
}