package com.timeular.nytta.email.core

import java.time.ZonedDateTime
import java.util.*

open class SendAndPersistMailService(
    private val dbPersistentMailService: DbPersistentMailService,
    private val mailgunMailService: MailgunMailService,
    private val persistMails: Boolean,
    private val sendMails: Boolean
) : MailService {
    override fun sendMail(mailConfig: MailConfig): Boolean {
        var result = false
        if (sendMails) {
            result = mailgunMailService.sendMail(mailConfig)
        }

        if (persistMails) {
            result = result.xor(sendMails).not() && dbPersistentMailService.sendMail(mailConfig)
        }

        return result
    }

    override fun sendMail(
        mailTemplate: MailTemplate,
        mailContext: Map<String, Any>,
        receiver: Set<MailContact>,
        deliveryTime: ZonedDateTime?,
        inlineAttachments: List<Attachment>,
        locale: Locale
    ): Boolean {
        var result = false
        if (sendMails) {
            result = mailgunMailService.sendMail(
                mailTemplate = mailTemplate,
                mailContext = mailContext,
                receiver = receiver,
                deliveryTime = deliveryTime,
                inlineAttachments = inlineAttachments,
                locale = locale
            )
        }

        if (persistMails) {
            result = result.xor(sendMails).not() && dbPersistentMailService.sendMail(
                mailTemplate = mailTemplate,
                mailContext = mailContext,
                receiver = receiver,
                deliveryTime = deliveryTime,
                inlineAttachments = inlineAttachments,
                locale = locale
            )
        }

        return result
    }
}