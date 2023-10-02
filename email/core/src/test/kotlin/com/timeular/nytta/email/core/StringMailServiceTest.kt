package com.timeular.nytta.email.core

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.util.*

class StringMailServiceTest {
    companion object {
        const val DE_TXT_CONTENT = "Wow eine Textvorlage für Johann!!"
        const val DE_HTML_CONTENT = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Beispiel Vorlage</title>
</head>
<body>

<h1>Hallo <span>Johann</span></h1>

</body>
</html>"""
        const val DE_AT_TXT_CONTENT = "Na Servas eine Textvorlage für Johann!!"
        const val DE_AT_HTML_CONTENT = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Beispiel Vorlage</title>
</head>
<body>

<h1>Griaß Di <span>Johann</span></h1>

</body>
</html>"""
    }

    private val mailService = StringMailService(
            mailTemplateContentBuilder = MailTemplateContentBuilder(
                    templateEngine()
            ),
            mailServiceHelper = MailServiceHelper()
    )

    private val mailTemplate = MailTemplate(
            mailConfigBuilder = MailConfig.Builder()
                    .from("hi@example.com", "Hi")
                    .subject("Hello Test"),
            htmlTemplate = "test_template.html",
            txtTemplate = "test_template.txt"
    )

    @Test
    fun testBuildLocalized() {
        sendMail(
                Locale.ENGLISH,
                MailTemplateContentBuilderTest.EN_HTML_CONTENT,
                MailTemplateContentBuilderTest.EN_TXT_CONTENT
        )

        sendMail(
                Locale.GERMAN,
                DE_HTML_CONTENT,
                DE_TXT_CONTENT
        )

        sendMail(
                Locale.forLanguageTag("de-AT"),
                DE_AT_HTML_CONTENT,
                DE_AT_TXT_CONTENT
        )

        sendMail(
                Locale.FRANCE,
                MailTemplateContentBuilderTest.EN_HTML_CONTENT,
                MailTemplateContentBuilderTest.EN_TXT_CONTENT
        )
    }

    private fun sendMail(locale: Locale, expectedHtml: String, expectedTxt: String) {
        mailService.sendMail(
                mailTemplate = mailTemplate,
                mailContext = mapOf("user" to MailTemplateContentBuilderTest.createWeeklyUser()),
                receiver = setOf(MailContact("some@email.copm")),
                locale = locale
        )
        val lastMail = mailService.mailConfig.get()
        mailService.mailConfig.remove()
        assertThat(lastMail.html, equalTo(expectedHtml))
        assertThat(lastMail.text, equalTo(expectedTxt))
        assertThat(lastMail.locale, equalTo(locale))
    }

}
