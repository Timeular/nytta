package com.timeular.nytta.email.core

import java.time.ZonedDateTime

abstract class AbstractMailService(
        private val mailTemplateContentBuilder: MailTemplateContentBuilder,
        private val mailServiceHelper: MailServiceHelper
) : MailService {

    override fun sendMail(
            mailTemplate: MailTemplate,
            mailContext: Map<String, Any>,
            receiver: Set<MailContact>,
            deliveryTime: ZonedDateTime?,
            inlineAttachments: List<Attachment>
    ): Boolean {

        var mailCfgBuilder = MailConfig.Builder.from(mailTemplate.mailConfigBuilder)

        receiver.forEach {
            mailCfgBuilder.addTo(it.email, it.name)
        }

        val correctedCtx = mailServiceHelper.modifyTemplateContextForOverride(mailContext, mailCfgBuilder)

        mailCfgBuilder = mailServiceHelper.modifyMailConfigBuilder(mailCfgBuilder)

        if(mailCfgBuilder.isNoEmailProvided()){
            return false
        }

        mailCfgBuilder.deliveryTime(deliveryTime)

        mailTemplate.htmlTemplate?.run {
            mailCfgBuilder.html(mailTemplateContentBuilder.build(mailTemplate.htmlTemplate, correctedCtx))
        }

        mailTemplate.txtTemplate?.run {
            mailCfgBuilder.text(mailTemplateContentBuilder.build(mailTemplate.txtTemplate, correctedCtx))
        }

        inlineAttachments.forEach {
            mailCfgBuilder.addInlineAttachment(it)
        }

        return sendMail(mailCfgBuilder.build())
    }
}