package com.timeular.nytta.email.core

import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AsyncMailService @JvmOverloads constructor(
        private val mailService: MailService,
        private val executor: Executor = Executors.newFixedThreadPool(1)
) : MailService {

    companion object {
        private val logger = LoggerFactory.getLogger(AsyncMailService::class.java)
    }

    override fun sendMail(mailConfig: MailConfig): Boolean {
        executor.run {
            try {
                mailService.sendMail(mailConfig)
            } catch (ex: Throwable) {
                logger.error("Unable to send email: `{}`", mailConfig.subject, ex)
            }
        }

        return true
    }

    override fun sendMail(
            mailTemplate: MailTemplate,
            mailContext: Map<String, Any>,
            receiver: Set<MailContact>,
            deliveryTime: ZonedDateTime?,
            inlineAttachments: List<Attachment>,
            locale: Locale
    ): Boolean {
        executor.run {
            try {
                mailService.sendMail(
                        mailTemplate = mailTemplate,
                        mailContext = mailContext,
                        receiver = receiver,
                        deliveryTime = deliveryTime,
                        inlineAttachments = inlineAttachments,
                        locale = locale
                )
            } catch (ex: Throwable) {
                logger.error("Unable to send email: `{}`", mailTemplate.htmlTemplate, ex)
            }
        }

        return true
    }
}