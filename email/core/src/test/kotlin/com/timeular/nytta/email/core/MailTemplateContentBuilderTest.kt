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

    @Test
    fun testBuild() {
        val ctx = mapOf<String, Any>("user" to createWeeklyUser())

        assertThat(
                mailTemplateContentBuilder.build("test_template.txt", ctx),
                equalTo("Wow a text template for Johann!!")
        )

        assertThat(
                mailTemplateContentBuilder.build("test_template.html", ctx),
                equalTo("""<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sample Template</title>
</head>
<body>

<h1>Hi there <span>Johann</span></h1>

</body>
</html>""")
        )
    }

    private fun createWeeklyUser() =
            User(
                    firstName = "Johann"
            )
}