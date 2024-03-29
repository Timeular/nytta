package com.timeular.nytta.ihop

import org.apache.commons.lang3.LocaleUtils
import java.util.*

@JvmOverloads
fun parseLocale(locale: String?, default: Locale? = null): Locale? =
    locale?.let {
        if (locale.isBlank()) {
            default
        } else {
            try {
                LocaleUtils.toLocale(locale.trim())
            } catch (ex: IllegalArgumentException) {
                val loc = Locale.forLanguageTag(locale.trim())
                if (Locale.getAvailableLocales().any { it == loc && it.toLanguageTag() != "und" }) {
                    loc
                } else {
                    default
                }

            }
        }
    } ?: default
