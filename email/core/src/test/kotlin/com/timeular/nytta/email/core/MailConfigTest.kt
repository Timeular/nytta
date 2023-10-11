package com.timeular.nytta.email.core

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class MailConfigTest {

    private lateinit var builder: MailConfig.Builder

    @BeforeEach
    fun beforeEach() {
        builder = MailConfig.Builder()
    }

    @Test
    fun testBuilderWithNoAttributes() {
        assertThrows(MailConfigurationException::class.java) { builder.build() }
    }

    @Test
    fun testBuilderWithSubjectOnly() {
        builder.from("test@me.com").subject("test")
        assertThrows(MailConfigurationException::class.java) { builder.build() }
    }

    @Test
    fun testBuilderWithMissingFromOnly() {
        builder.from("test@me.com")
        assertThrows(MailConfigurationException::class.java) { builder.build() }
    }

    @Test
    fun testBuilderIsNoEmailProvided() {
        assertThat(builder.isNoEmailProvided(), equalTo(true))

        builder.addTo("test@me.com")
        assertThat(builder.isNoEmailProvided(), equalTo(false))

        builder.clearTo()
        assertThat(builder.isNoEmailProvided(), equalTo(true))
        builder.addCC("test@me.com")
        assertThat(builder.isNoEmailProvided(), equalTo(false))

        builder.clearCC()
        assertThat(builder.isNoEmailProvided(), equalTo(true))
        builder.addBCC("test@me.com")
        assertThat(builder.isNoEmailProvided(), equalTo(false))
    }

    @Test
    fun testBuilderWithMissingReceiver() {
        builder.from("test@me.com").subject("Test")
                .text("text")

        // missing to, cc, bcc
        builder.clearTo()
        assertThrows(MailConfigurationException::class.java) { builder.build() }

        // test To
        builder.addTo("test@me.com")
        builder.build()
        builder.clearTo()
        assertThrows(MailConfigurationException::class.java) { builder.build() }

        // test cc
        builder.addCC("test@me.com")
        builder.build()
        builder.clearCC()
        assertThrows(MailConfigurationException::class.java) { builder.build() }

        // test bcc
        builder.addBCC("test@me.com")
        builder.build()
        builder.clearBCC()
        assertThrows(MailConfigurationException::class.java) { builder.build() }
    }

    @Test
    fun testBuilderWithMissingContent() {
        builder.from("test@me.com")
                .subject("test")
                .addTo("receiver@me.com")

        //missing text & html
        assertThrows(MailConfigurationException::class.java) { builder.build() }

        // test text
        builder.text("text")
        builder.build()
        builder.text("")
        assertThrows(MailConfigurationException::class.java) { builder.build() }

        // test html
        builder.html("html")
        builder.build()
        builder.html("")
        assertThrows(MailConfigurationException::class.java) { builder.build() }
    }

    @Test
    fun testMailConfig() {
        val cfg = builder.from("support@timeular.com", "Timeular Support Team")
                .subject("Test Message")
                .text("If everything seems to be going well, you have obviously overlooked something.")
                .html("Matter will be damaged in direct proportion to its value.")
                .addTo("first@test.com")
                .addCC("cc@test.com")
                .addBCC("bcc@test.com")
                .build()

        assertThat(cfg.text, equalTo("If everything seems to be going well, you have obviously overlooked something."))
        assertThat(cfg.html, equalTo("Matter will be damaged in direct proportion to its value."))
        assertThat(cfg.subject, equalTo("Test Message"))
        assertThat(cfg.from.toString(), equalTo("Timeular Support Team <support@timeular.com>"))

        assertThat(cfg.to, hasSize(equalTo(1)))
        assertThat(cfg.to[0].toString(), equalTo("first@test.com"))

        assertThat(cfg.cc, hasSize(equalTo(1)))
        assertThat(cfg.cc[0].toString(), equalTo("cc@test.com"))

        assertThat(cfg.bcc, hasSize(equalTo(1)))
        assertThat(cfg.bcc[0].toString(), equalTo("bcc@test.com"))
    }

    @Test
    fun testBuilderWithSubject() {
        builder.from("support@timeular.com", "Timeular Support Team")
            .subject("Default Message")
            .subject("English Message", Locale.ENGLISH)
            .subject("US Message", Locale.US)
            .subject("German Message", Locale.GERMAN)
            .addTo("first@test.com")
            .text("If everything seems to be going well, you have obviously overlooked something.")

        with(builder.build()) {
            assertThat(subject, equalTo("Default Message"))
        }

        with(builder.build(Locale.ENGLISH)) {
            assertThat(subject, equalTo("English Message"))
        }

        with(builder.build(Locale.UK)) {
            assertThat(subject, equalTo("English Message"))
        }

        with(builder.build(Locale.US)) {
            assertThat(subject, equalTo("US Message"))
        }

        with(builder.build(Locale.GERMAN)) {
            assertThat(subject, equalTo("German Message"))
        }

        with(builder.build(Locale.FRANCE)) {
            assertThat(subject, equalTo("Default Message"))
        }
    }
}