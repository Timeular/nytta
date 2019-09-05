package com.timeular.nytta.email.core

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

internal class MailgunMailServiceTest {
    private val mailgunMailService = MailgunMailService(
            mailTemplateContentBuilder = MailTemplateContentBuilder(
                    mailTemplateEngine = templateEngine()
            ),
            mailServiceHelper = MailServiceHelper(),
            baseUrl = "set-if-you-want-to-test",
            apiKey = "set-if-you-want-to-test",
            domain = "set-if-you-want-to-test"
    )

    private val tmpl = MailTemplate(
            mailConfigBuilder = MailConfig.Builder(),
            htmlTemplate = "test_template.html",
            txtTemplate = "test_template.txt"
    )

    @Test
    @Disabled
    fun testSendMail() {
        val cfg = MailConfig.Builder()
                .from("support@timeular.com", "Support")
                .subject("Test Email from Code")
                .text("Yeah! Emails are sending!")
                .addTo("sp@timeular.com")
                .deliveryTime(ZonedDateTime.parse("2018-08-13T22:10:00+02:00"))
                .tag("unitTest")
                .build()

        assertThat(
                mailgunMailService.sendMail(
                        mailConfig = cfg
                ),
                equalTo(true)
        )
    }

    @Test
    @Disabled
    fun testSendMailFromTemplate() {
        val ctx = HashMap<String, Any>()
        ctx["user"] = "User"

        assertThat(
                mailgunMailService.sendMail(
                        mailTemplate = tmpl,
                        mailContext = ctx,
                        receiver = setOf(MailContact("sp+test@timeular.com")),
                        deliveryTime = ZonedDateTime.parse("2018-08-12T10:00:00+02:00")
                ),
                equalTo(true)
        )
    }
}