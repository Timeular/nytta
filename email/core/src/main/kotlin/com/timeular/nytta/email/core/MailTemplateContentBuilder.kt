package com.timeular.nytta.email.core

import org.thymeleaf.ITemplateEngine
import org.thymeleaf.context.Context
import java.util.*

open class MailTemplateContentBuilder(
        private val mailTemplateEngine: ITemplateEngine
) {
    fun build(template: String, context: Map<String, Any>, locale: Locale = Locale.ENGLISH): String =
            mailTemplateEngine.process(template, Context(locale, context))

}
