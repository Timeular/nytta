package com.timeular.nytta.email.core

import java.time.ZonedDateTime
import java.util.*

interface MailService {

    fun sendMail(mailConfig: MailConfig): Boolean

    fun sendMail(
        mailTemplate: MailTemplate,
        mailContext: Map<String, Any>,
        receiver: Set<MailContact>,
    ): Boolean = sendMail(
        mailTemplate = mailTemplate,
        mailContext = mailContext,
        receiver = receiver,
        locale = Locale.ENGLISH
    )

    fun sendMail(
        mailTemplate: MailTemplate,
        mailContext: Map<String, Any>,
        receiver: Set<MailContact>,
        locale: Locale = Locale.ENGLISH
    ): Boolean = sendMail(
        mailTemplate = mailTemplate,
        mailContext = mailContext,
        receiver = receiver,
        deliveryTime = null,
        locale = locale
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
        locale = Locale.ENGLISH
    )

    fun sendMail(
        mailTemplate: MailTemplate,
        mailContext: Map<String, Any>,
        receiver: Set<MailContact>,
        deliveryTime: ZonedDateTime,
        locale: Locale = Locale.ENGLISH
    ): Boolean = sendMail(
        mailTemplate = mailTemplate,
        mailContext = mailContext,
        receiver = receiver,
        deliveryTime = deliveryTime,
        inlineAttachments = emptyList(),
        locale = locale
    )

    fun sendMail(
        mailTemplate: MailTemplate,
        mailContext: Map<String, Any>,
        receiver: Set<MailContact>,
        inlineAttachments: List<Attachment> = emptyList(),
        locale: Locale = Locale.ENGLISH
    ): Boolean = sendMail(
        mailTemplate = mailTemplate,
        mailContext = mailContext,
        receiver = receiver,
        inlineAttachments = inlineAttachments,
        deliveryTime = null,
        locale = locale
    )

    fun sendMail(
        mailTemplate: MailTemplate,
        mailContext: Map<String, Any>,
        receiver: Set<MailContact>,
        deliveryTime: ZonedDateTime? = null,
        inlineAttachments: List<Attachment> = emptyList(),
        locale: Locale = Locale.ENGLISH
    ): Boolean
}
