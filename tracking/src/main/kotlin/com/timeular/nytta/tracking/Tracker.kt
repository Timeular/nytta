package com.timeular.nytta.tracking

/**
 * Main Interface for all tracker implementation. Here you have two possibilities (if supported by the underlying tacker).
 *
 * 1. Track Event
 * 2. Add additional data to a user profile
 *
 */
interface Tracker {

    fun trackEvent(identifier: String, event: String) = trackEvent(
            identifier = identifier,
            event = event,
            additionalData = null
    )

    fun trackEvent(
            identifier: String,
            event: String,
            additionalData: Map<String, String>?
    )

    fun updateUserProfile(
            identifier: String,
            dataKey: String,
            dataValue: String
    )

    fun updateUserProfile(identifier: String, userData: Map<String, String>)
}