package com.timeular.nytta.email.core

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

data class User(
        val firstName: String
)

internal class MailTemplateContentBuilderTest {
    private val mailTemplateContentBuilder = MailTemplateContentBuilder(
            mailTemplateEngine = templateEngine()
    )

    companion object {
        const val EN_TXT_CONTENT = "Wow a text template for Johann!!"
        const val EN_HTML_CONTENT = """<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sample Template</title>
</head>
<body>

<h1>Hi there <span>Johann</span></h1>

</body>
</html>"""

        fun createWeeklyUser(): User =
                User(
                        firstName = "Johann"
                )
    }

    @Test
    fun testBuild() {
        val ctx = mapOf<String, Any>("user" to createWeeklyUser())

        assertThat(
                mailTemplateContentBuilder.build("test_template.txt", ctx),
                equalTo(EN_TXT_CONTENT)
        )

        assertThat(
                mailTemplateContentBuilder.build("test_template.html", ctx),
                equalTo(EN_HTML_CONTENT)
        )
    }
}