package com.timeular.nytta.tracking

/**
 * Basic "context" to have a container for static access to the tracking instance including a small
 * feature flag.
 */
class TrackerContext : Tracker {
    lateinit var tracker: Tracker
    var enabled: Boolean = false

    companion object {
        private val instance = TrackerContext()

        @JvmStatic
        fun initialize(tracker: Tracker, enabled: Boolean) {
            instance.tracker = tracker
            instance.enabled = enabled
        }

        @JvmStatic
        fun track(identifier: String, event: String) = track(
            identifier = identifier,
            event = event,
            additionalData = null
        )

        @JvmStatic
        fun track(identifier: String, event: String, additionalData: Map<String, String>?) {
            instance.trackEvent(identifier, event, additionalData)
        }

        @JvmStatic
        fun updateProfile(identifier: String, dataKey: String, dataValue: String) {
            instance.updateUserProfile(identifier, dataKey, dataValue)
        }

        @JvmStatic
        fun updateProfile(identifier: String, userData: Map<String, String>) {
            instance.updateUserProfile(identifier, userData)
        }

        @JvmStatic
        fun addAlias(identifier: String, alias: String) {
            instance.createAlias(identifier, alias)
        }
    }

    override fun trackEvent(identifier: String, event: String, additionalData: Map<String, String>?) {
        executeIfEnabled {
            tracker.trackEvent(identifier, event, additionalData)
        }
    }

    override fun updateUserProfile(identifier: String, dataKey: String, dataValue: String) {
        executeIfEnabled {
            tracker.updateUserProfile(identifier, dataKey, dataValue)
        }
    }

    override fun updateUserProfile(identifier: String, userData: Map<String, String>) {
        executeIfEnabled {
            tracker.updateUserProfile(identifier, userData)
        }
    }

    override fun createAlias(identifier: String, alias: String) {
        executeIfEnabled {
            tracker.createAlias(identifier, alias)
        }
    }

    private fun executeIfEnabled(fn: () -> Unit) {
        if (enabled) {
            fn()
        }
    }
}