package com.timeular.nytta.tracking

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.timeular.nytta.http.client.OkHttpClient
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


internal class MixpanelTrackerTest {

    companion object {
       // private const val API_TOKEN = "change-that-before-you-run-the-test"
        private const val API_TOKEN ="c71719bab4e437138f1422faaf6e91a8"
        private const val GDPR_TOKEN = "change-that-before-you-run-the-test"
    }

    private val tracker = MixpanelTracker(
            token = "token",
            httpClient = OkHttpClient(),
            gdprToken = "token"
    )

    @Test
    fun testBuildTrackingData() {
        assertThat(
                tracker.buildTrackingData("userId", "event", null),
                equalTo("eyJldmVudCI6ImV2ZW50IiwicHJvcGVydGllcyI6eyJkaXN0aW5jdF9pZCI6InVzZXJJZCIsInRva2VuIjoidG9rZW4ifX0=")
        )
    }

    @Test
    fun testBuildUpdateUserData() {
        assertThat(
                tracker.buildUpdateUserData("userId", mapOf("property" to "data")),
                equalTo("eyIkdG9rZW4iOiJ0b2tlbiIsIiRkaXN0aW5jdF9pZCI6InVzZXJJZCIsIiRzZXQiOnsicHJvcGVydHkiOiJkYXRhIn19")
        )
    }

    /**
     * this test is for manual testing - pls change the api_token to execute the test
     * locally
     */
    @Test
    @Disabled
    fun testTrackEvent() {
        MixpanelTracker(
                token = API_TOKEN,
                gdprToken = GDPR_TOKEN,
                httpClient = OkHttpClient()
        ).trackEvent("spucbert", "super event")

        MixpanelTracker(
                token = API_TOKEN,
                gdprToken = GDPR_TOKEN,
                httpClient = OkHttpClient()
        ).trackEvent("spucbert", "super event", mapOf("someKey" to "someValue"))
        Thread.sleep(500)
    }

    /**
     * this test is for manual testing - pls change the api_token to execute the test
     * locally
     */
    @Test
   // @Disabled
    fun testUpdateUser() {
        MixpanelTracker(
                token = API_TOKEN,
                gdprToken = GDPR_TOKEN,
                httpClient = OkHttpClient()
        ).updateUserProfile("spucbert", "email", "spuc.bert@timeular.com")


        MixpanelTracker(
                token = API_TOKEN,
                gdprToken = GDPR_TOKEN,
                httpClient = OkHttpClient()
        ).updateUserProfile("spucbert", "nestedStructure", "{\"key1\": \"value1\", \"key2\": [ \"array1\", \"array2\"]}")
        Thread.sleep(500)
    }

    @Test
    @Disabled
    fun testDeleteUser() {
        MixpanelTracker(
                token = API_TOKEN,
                gdprToken = GDPR_TOKEN,
                httpClient = OkHttpClient()
        ).deleteUserProfile("spucbert")
    }
}