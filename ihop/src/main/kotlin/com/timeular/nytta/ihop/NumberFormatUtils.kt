package com.timeular.nytta.ihop

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Unifies all rounding and number formatting issues.
 */
class NumberFormatUtils private constructor() {

    companion object {
        val instance = NumberFormatUtils()

        fun formatDouble(value: Double) = instance.formatDouble(value)

        fun formatBigDecimal(value: BigDecimal): String = instance.formatBigDecimal(value)

        fun round(value: Int) = instance.round(value)

        fun round(value: Double) = instance.round(value)

        fun round(value: BigDecimal) = instance.round(value)
    }

    fun formatDouble(value: Double): String =
            formatBigDecimal(value.toBigDecimal())

    fun formatBigDecimal(value: BigDecimal): String =
            round(value)
                    .toString()
                    .removeSuffix(".0")

    fun round(value: Double): BigDecimal =
            round(value.toBigDecimal())

    fun round(value: BigDecimal): BigDecimal =
            value.setScale(1, RoundingMode.HALF_UP)

    fun round(value: Int): BigDecimal =
            round(value.toBigDecimal())
}