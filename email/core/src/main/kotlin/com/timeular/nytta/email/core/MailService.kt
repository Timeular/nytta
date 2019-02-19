package com.timeular.nytta.email.core

import java.time.ZonedDateTime

interface MailService {

    fun sendMail(mailConfig: MailConfig): Boolean

    fun sendMail(
            mailTemplate: MailTemplate,
            mailContext: Map<String, Any>,
            receiver: Set<MailContact>
    ): Boolean = sendMail(
            mailTemplate = mailTemplate,
            mailContext = mailContext,
            receiver = receiver,
            deliveryTime = null
    )

    fun sendMail(
            mailTemplate: MailTemplate,
            mailContext: Map<String, Any>,
            receiver: Set<MailContact>,
            deliveryTime: ZonedDateTime
    ): Boolean = sendMail(
            mailTemplate = mailTemplate,
            mailContext = mailContext,
            receiver = receiver,
            deliveryTime = deliveryTime,
            inlineAttachments = emptyList()
    )

    fun sendMail(
            mailTemplate: MailTemplate,
            mailContext: Map<String, Any>,
            receiver: Set<MailContact>,
            inlineAttachments: List<Attachment> = emptyList()
    ): Boolean = sendMail(
            mailTemplate = mailTemplate,
            mailContext = mailContext,
            receiver = receiver,
            inlineAttachments = inlineAttachments,
            deliveryTime = null
    )

    fun sendMail(
            mailTemplate: MailTemplate,
            mailContext: Map<String, Any>,
            receiver: Set<MailContact>,
            deliveryTime: ZonedDateTime? = null,
            inlineAttachments: List<Attachment> = emptyList()
    ): Boolean
}
