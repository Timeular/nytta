package com.timeular.nytta.tracking

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import com.timeular.nytta.http.client.HttpClient
import com.timeular.nytta.http.client.HttpMethod
import com.timeular.nytta.http.client.OkHttpClient
import com.timeular.nytta.http.client.UrlBuilder
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MixpanelTracker @JvmOverloads constructor(
        private val httpClient: HttpClient,
        private val token: String,
        private val gdprToken: String? = null,
        private val executor: Executor = Executors.newFixedThreadPool(1)
) : Tracker {

    companion object {
        private const val BASE_URL = "https://api.mixpanel.com"
        private const val TRACKING_URL = "$BASE_URL/track/"
        private const val UPDATE_USER_URL = "$BASE_URL/engage/"
        private const val RESPONSE_SUCCESS = "1"
        private const val DELETE_URL = "https://mixpanel.com/api/app/data-deletions/v2.0/"
        private const val APPLICATION_JSON_VALUE = "application/json"

        private val logger = LoggerFactory.getLogger(MixpanelTracker::class.java)
    }


    private val extHttpClient = OkHttpClientExt()

    override fun trackEvent(identifier: String, event: String, additionalData: Map<String, String>?) {
        sendData(
                url = TRACKING_URL,
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
        sendData(
                url = UPDATE_USER_URL,
                data = buildUpdateUserData(identifier, userData),
                errorSubject = "Unable to update userprofile for user ($identifier)"
        )
    }

    fun deleteUserProfile(identifier: String) {
        if (gdprToken == null) {
            throw TrackerException("You have to configure the gdpr token if you want to use the delete function")
        }

        val url = UrlBuilder.newBuilder()
                .url(DELETE_URL)
                .addUrlParameter("token", token)
                .build()

        val resp = extHttpClient.request(
                httpMethod = HttpMethod.POST,
                url = url,
                headers = mapOf(
                        "Authorization" to "Bearer $gdprToken",
                        "Content-Type" to APPLICATION_JSON_VALUE
                ),
                bodyJson = jsonObject("distinct_ids" to jsonArray(identifier))
        )

        if (resp.wasSuccessful()) {
            val taskId = resp.body.asJsonObject["results"].asJsonObject["task_id"].asString
            logger.info("User deletion process started on mixpanel - user: {} - taskId: {}", identifier, taskId)
        } else {
            throw TrackerException("Unable to delete user $identifier from mixpanel: status code ${resp.code}")
        }
    }

    private fun sendData(url: String, data: String, errorSubject: String) {
        executor.execute {
            try {
                val mixpanelUrl = UrlBuilder.newBuilder()
                        .addUrlParameter("data", data)
                        .url(url)
                        .build()

                val response = httpClient.requestWithTextResponse(
                        httpMethod = HttpMethod.GET,
                        url = mixpanelUrl
                )

                if (response.wasResponseError()) {
                    logger.warn("{}: HttpStatus Code: {}", errorSubject, response.code)
                }

                if (response.body != RESPONSE_SUCCESS) {
                    logger.warn("{}: response: {}", errorSubject, response.body)
                }
            } catch (ex: Exception) {
                logger.warn("Unable to send data to mixpanel", ex)
            }
        }
    }

    internal fun buildTrackingData(identifier: String, event: String, additionalData: Map<String, String>?): String {
        val properties = jsonObject(
                "distinct_id" to identifier,
                "token" to token
        )

        additionalData?.forEach { k, v -> properties.addProperty(k, v) }

        return jsonObject(
                "event" to event,
                "properties" to properties
        ).toBase64String()
    }

    internal fun buildUpdateUserData(identifier: String, userData: Map<String, String>) =
            jsonObject(
                    "\$token" to token,
                    "\$distinct_id" to identifier,
                    "\$set" to jsonObject(userData.map { Pair(it.key, it.value) })
            ).toBase64String()

    private fun JsonObject.toBase64String() =
            String(
                    Base64.getEncoder().encode(this.toString().toByteArray(Charsets.UTF_8)),
                    StandardCharsets.ISO_8859_1
            )

    private class OkHttpClientExt : OkHttpClient(60) {
        override fun requestBodyWith(bodyJsonAsText: String): okhttp3.RequestBody =
                bodyJsonAsText.toByteArray().toRequestBody(APPLICATION_JSON_VALUE.toMediaType())
    }
}