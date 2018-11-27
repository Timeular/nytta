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

private val dateFormatterWithTimeZone = createDateTimeFormatter(true)
private val dateFormatterWithoutTimeZone = createDateTimeFormatter(false)

private fun createDateTimeFormatter(withTimeZoneOffset: Boolean): DateTimeFormatter {
    val builder = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(ChronoField.YEAR, 4)
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral('T')
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .appendLiteral('.')
            .appendValue(ChronoField.MILLI_OF_SECOND, 3)

    if (withTimeZoneOffset) builder.appendOffset("+HHMM", "+0000")
    return builder
            .toFormatter(Locale.UK)
            .withResolverStyle(ResolverStyle.STRICT)
}

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