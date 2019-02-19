package com.timeular.nytta.email.core

import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashMap

/**
 * this mail service is only for generating mails and saving the outcome in an thread safe variable
 */
class StringMailService(
        mailTemplateContentBuilder: MailTemplateContentBuilder,
        mailServiceHelper: MailServiceHelper
) : AbstractMailService(mailTemplateContentBuilder, mailServiceHelper) {

    val mailConfig = ThreadLocal<MailConfig>()

    override fun sendMail(
            mailTemplate: MailTemplate,
            mailContext: Map<String, Any>,
            receiver: Set<MailContact>,
            deliveryTime: ZonedDateTime?,
            inlineAttachments: List<Attachment>
    ): Boolean {

        val enhancedMailContext = HashMap<String, Any>(mailContext)

        inlineAttachments.forEach {
            enhancedMailContext[it.name.replace(".", "")] = createDataUrl(it)
        }

        mailTemplate.mailConfigBuilder.inlineAttachments.forEach {
            enhancedMailContext[it.name.replace(".", "")] = createDataUrl(it)
        }

        return super.sendMail(mailTemplate, enhancedMailContext, receiver, deliveryTime, inlineAttachments)
    }

    override fun sendMail(mailConfig: MailConfig): Boolean {
        this.mailConfig.set(mailConfig)
        return true
    }

    private fun createDataUrl(attachment: Attachment): String {
        val content = attachment.resource
        val base64Img = Base64.getEncoder().encode(content).toString(Charsets.UTF_8)

        return "data:${attachment.mimeType};base64,$base64Img"
    }
}