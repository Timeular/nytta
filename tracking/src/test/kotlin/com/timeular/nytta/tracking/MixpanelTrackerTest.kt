package com.timeular.nytta.tracking

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


internal class MixpanelTrackerTest {

    companion object {
        private const val API_TOKEN = "change-that-before-you-run-the-test"
        private const val IDENTIFIER = "spucman"
    }

    private val tracker = MixpanelTracker(
        token = "token",
        httpClient = OkHttpClient()
    )

    @Test
    fun testBuildTrackingData() {
        assertThat(
            tracker.buildTrackingData(IDENTIFIER, "event", null),
            equalTo("{\"event\":\"event\",\"properties\":{\"distinct_id\":\"$IDENTIFIER\",\"token\":\"token\"}}")
        )
    }

    @Test
    fun testBuildUpdateUserData() {
        assertThat(
            tracker.buildUpdateUserData(IDENTIFIER, mapOf("property" to "data")),
            equalTo("{\"\$token\":\"token\",\"\$distinct_id\":\"$IDENTIFIER\",\"\$set\":{\"property\":\"data\"}}")
        )
    }

    @Test
    fun testBuildDeleteUserData() {
        assertThat(
            tracker.buildDeleteUserData(IDENTIFIER),
            equalTo("{\"\$token\":\"token\",\"\$distinct_id\":\"$IDENTIFIER\",\"\$delete\":\"\",\"\$ignore_alias\":false}")
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
            httpClient = OkHttpClient()
        ).trackEvent(IDENTIFIER, "super event")

        MixpanelTracker(
            token = API_TOKEN,
            httpClient = OkHttpClient()
        ).trackEvent(IDENTIFIER, "super event", mapOf("someKey" to "someValue"))
        Thread.sleep(500)
    }

    /**
     * this test is for manual testing - pls change the api_token to execute the test
     * locally
     */
    @Test
    @Disabled
    fun testUpdateUser() {
        MixpanelTracker(
            token = API_TOKEN,
            httpClient = OkHttpClient()
        ).updateUserProfile(IDENTIFIER, "\$email", "$IDENTIFIER@example.com")


        MixpanelTracker(
            token = API_TOKEN,
            httpClient = OkHttpClient()
        ).updateUserProfile(
            IDENTIFIER,
            "nestedStructure",
            "{\"key1\": \"value1\", \"key2\": [ \"array1\", \"array2\"]}"
        )
        Thread.sleep(500)
    }

    @Test
    @Disabled
    fun testDeleteUser() {
        MixpanelTracker(
            token = API_TOKEN,
            httpClient = OkHttpClient()
        ).deleteUserProfile(IDENTIFIER)
    }
}
