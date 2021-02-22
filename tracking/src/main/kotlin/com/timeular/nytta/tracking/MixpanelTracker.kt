package com.timeular.nytta.tracking

import com.github.salomonbrys.kotson.jsonObject
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * See https://developer.mixpanel.com/reference
 */
class MixpanelTracker @JvmOverloads constructor(
    private val httpClient: OkHttpClient,
    private val token: String,
    private val area: Area = Area.EU,
    private val executor: Executor = Executors.newFixedThreadPool(1)
) : Tracker {

    companion object {
        private const val US_BASE_URL = "https://api.mixpanel.com"
        private const val EU_BASE_URL = "https://api-eu.mixpanel.com"

        private const val TRACKING_URI = "/track#live-event"
        private const val UPDATE_PROFILE_URI = "/engage#profile-set"
        private const val DELETE_PROFILE_URI = "/engage#profile-delete"

        private const val RESPONSE_SUCCESS = "1"
        private const val TEXT_PLAIN = "text/plain"

        private val logger = LoggerFactory.getLogger(MixpanelTracker::class.java)
    }

    enum class Area {
        US,
        EU
    }

    private val trackingUrl: String
    private val updateProfileUrl: String
    private val deleteProfileUrl: String

    init {
        if (area == Area.EU) {
            trackingUrl = "$EU_BASE_URL$TRACKING_URI"
            updateProfileUrl = "$EU_BASE_URL$UPDATE_PROFILE_URI"
            deleteProfileUrl = "$EU_BASE_URL$DELETE_PROFILE_URI"
        } else {
            trackingUrl = "$US_BASE_URL$TRACKING_URI"
            updateProfileUrl = "$US_BASE_URL$UPDATE_PROFILE_URI"
            deleteProfileUrl = "$US_BASE_URL$DELETE_PROFILE_URI"
        }
    }

    override fun trackEvent(identifier: String, event: String, additionalData: Map<String, String>?) {
        sendDataAsync(
            url = trackingUrl,
            data = buildTrackingData(identifier, event, additionalData),
            errorSubject = "Unable to track event '$event' for user ($identifier)"
        )
    }

    override fun updateUserProfile(
        identifier: String,
        dataKey: String,
        dataValue: String
    ) {
        updateUserProfile(identifier, mapOf(dataKey to dataValue))
    }

    override fun updateUserProfile(identifier: String, userData: Map<String, String>) {
        sendDataAsync(
            url = updateProfileUrl,
            data = buildUpdateUserData(identifier, userData),
            errorSubject = "Unable to update userprofile for user ($identifier)"
        )
    }

    fun deleteUserProfile(identifier: String) {
        sendData(
            url = deleteProfileUrl,
            data = buildDeleteUserData(identifier),
            errorSubject = "Unable to delete user ($identifier)"
        )
    }

    private fun sendDataAsync(url: String, data: String, errorSubject: String) {
        executor.execute {
            sendData(
                url = url,
                data = data,
                errorSubject = errorSubject
            )
        }
    }

    private fun sendData(url: String, data: String, errorSubject: String) {
        try {
            val request = Request.Builder()
                .url(url)
                .headers(
                    Headers.Builder()
                        .add("Accept", TEXT_PLAIN)
                        .build()
                )
                .post(FormBody.Builder().add("data", data).build())
                .build()

            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw TrackerException("$errorSubject: HttpStatus Code: ${response.code}")
                }

                response.body?.string()?.let { txtBody ->
                    if (txtBody != RESPONSE_SUCCESS) {
                        logger.warn("{}: response: {}", errorSubject, response.body)
                    }
                } ?: logger.warn("Unable to retrieve response for call $url")
            }
        } catch (ex: Exception) {
            logger.warn("Unable to send data to mixpanel", ex)
        }
    }

    internal fun buildTrackingData(identifier: String, event: String, additionalData: Map<String, String>?): String {
        val properties = jsonObject(
            "distinct_id" to identifier,
            "token" to token
        )

        additionalData?.forEach { (k, v) -> properties.addProperty(k, v) }

        return jsonObject(
            "event" to event,
            "properties" to properties
        ).toString()
    }

    internal fun buildUpdateUserData(identifier: String, userData: Map<String, String>): String =
        jsonObject(
            "\$token" to token,
            "\$distinct_id" to identifier,
            "\$set" to jsonObject(userData.map { Pair(it.key, it.value) })
        ).toString()

    internal fun buildDeleteUserData(identifier: String): String =
        jsonObject(
            "\$token" to token,
            "\$distinct_id" to identifier,
            "\$delete" to "",
            "\$ignore_alias" to false
        ).toString()
}
