package com.timeular.nytta.ihop.time

import com.timeular.nytta.ihop.toOrdinalString
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.ResolverStyle
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import java.time.temporal.IsoFields
import java.time.temporal.TemporalAdjusters
import java.util.*

const val NANO_TO_MILLIS = 1000000
const val MILLIS_TO_MIN = 1000 * 60
const val MILLIS_TO_HOURS = MILLIS_TO_MIN * 60
const val DATE_PATTERN_WITHOUT_TIMEZONE = "uuuu-MM-dd'T'HH:mm:ss.SSS"
const val DATE_PATTERN_WITH_TIMEZONE = "${DATE_PATTERN_WITHOUT_TIMEZONE}Z"

private val dateFormatterWithTimeZone = createDateTimeFormatter(DATE_PATTERN_WITH_TIMEZONE)
private val dateFormatterWithoutTimeZone = createDateTimeFormatter(DATE_PATTERN_WITHOUT_TIMEZONE)

private fun createDateTimeFormatter(pattern: String): DateTimeFormatter =
        DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern(pattern)
                .toFormatter(Locale.UK)
                .withResolverStyle(ResolverStyle.STRICT)

fun dateTimeFormatter(withTimeZoneOffset: Boolean): DateTimeFormatter =
        if (withTimeZoneOffset) {
            dateFormatterWithTimeZone
        } else {
            dateFormatterWithoutTimeZone
        }


fun formatDayMonthToString(date: ZonedDateTime): String {
    val formatter = DateTimeFormatterBuilder()
            .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL)
            .appendLiteral(" ")
            .appendLiteral(date.dayOfMonth.toOrdinalString())
            .toFormatter(Locale.UK)
            .withResolverStyle(ResolverStyle.STRICT)

    return formatter.format(date)
}

fun convertMillisToHours(millis: Long): Double =
        millis / MILLIS_TO_HOURS.toDouble()

fun convertHoursToMillis(hours: Double): Long =
        (hours * MILLIS_TO_HOURS).toLong()

fun String.toZonedDateTime(): ZonedDateTime =
        ZonedDateTime.from(dateTimeFormatter(true).parse(this))

fun firstDayOfWeekOfYear(weekOfYear: Int, year: Int, timezone: String): ZonedDateTime =
        ZonedDateTime.now(zoneOffsetFromTimezone(timezone))
                .withYear(year)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekOfYear.toLong())
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .truncatedTo(ChronoUnit.DAYS)

fun searchPreviousMonday(date: ZonedDateTime): ZonedDateTime =
        date.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
                .truncatedTo(ChronoUnit.DAYS)

fun isoDateFromString(date: String): Date = Date.from(
        LocalDateTime.parse(
                date,
                DateTimeFormatter.ISO_DATE_TIME
        ).toInstant(ZoneOffset.UTC)
)

fun String.toISODate(): Date =
        isoDateFromString(this)