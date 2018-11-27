package com.timeular.nytta.ihop

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