package com.timeular.nytta.ihop.time

import java.time.ZoneId
import java.time.ZoneOffset
import java.util.TimeZone
import java.util.regex.Pattern

const val UTC = "UTC+00:00"

const val UTC_PATTERN = "UTC[-+]([0][0-9]|[1][0-4]):([0][0]|[1][5]|[3][0]|[4][5])"

val pattern: Pattern = Pattern.compile(UTC_PATTERN)

fun String?.isUTCOffset(): Boolean =
        this?.let { isUTCOffsetString(this) } ?: false

fun isUTCOffsetString(source: String): Boolean = pattern.matcher(source).matches()

fun toUTCOffsetString(source: String): String {
    if (isUTCOffsetString(source)) return source
    val tz = TimeZone.getAvailableIDs()
            .filter { it.equals(source, true) }
            .getOrNull(0) ?: return UTC
    return toUTCOffsetString(TimeZone.getTimeZone(tz))
}

fun toUTCOffsetString(zone: ZoneId): String =
        toUTCOffsetString(TimeZone.getTimeZone(zone))

fun toUTCOffsetString(timezone: TimeZone): String {
    val offset = timezone.rawOffset / 1000 / 60
    val sign = if (offset < 0) "-" else "+"
    val hours = Math.abs(offset / 60)
    val minutes = Math.abs(offset % 60)

    return String.format("UTC%s%02d:%02d", sign, hours, minutes)
}

fun zoneOffsetFromTimezone(timezone: String): ZoneOffset =
        if (isUTCOffsetString(timezone)) {
            ZoneOffset.of(timezone.removePrefix("UTC"))
        } else {
            throw UnknownTimezoneException("Unknown timezone found: $timezone")
        }

class UnknownTimezoneException(message: String) : RuntimeException(message)