package com.timeular.nytta.email.core

import org.thymeleaf.ITemplateEngine
import org.thymeleaf.context.Context
import java.util.*

open class MailTemplateContentBuilder(
        private val mailTemplateEngine: ITemplateEngine
) {
    fun build(template: String, context: Map<String, Any>): String =
            mailTemplateEngine.process(template, Context(Locale.ENGLISH, context))
}
