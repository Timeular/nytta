package com.timeular.nytta.email.core

import java.time.ZonedDateTime

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

    override fun sendMail(mailTemplate: MailTemplate, mailContext: Map<String, Any>, receiver: Set<MailContact>,
                          deliveryTime: ZonedDateTime?, inlineAttachments: List<Attachment>): Boolean {
        var result = false
        if (sendMails) {
            result = mailgunMailService.sendMail(mailTemplate, mailContext, receiver, deliveryTime, inlineAttachments)
        }

        if (persistMails) {
            result = result.xor(sendMails).not() && dbPersistentMailService.sendMail(mailTemplate, mailContext, receiver, deliveryTime)
        }

        return result
    }
}