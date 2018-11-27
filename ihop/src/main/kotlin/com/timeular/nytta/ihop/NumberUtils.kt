package com.timeular.nytta.ihop

/**
 * converts the integer to its ordinal string representation
 */
fun Int.toOrdinalString(): String =
        "$this" + if (this % 100 in 11..13) {
            "th"
        } else {
            when (this % 10) {
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> "th"
            }
        }