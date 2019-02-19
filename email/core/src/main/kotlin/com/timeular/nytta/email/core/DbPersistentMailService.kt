package com.timeular.nytta.email.core

import com.timeular.nytta.email.core.db.MailStorageRepository

/**
 * this mail service stores the email inside a db instead of sending them
 */
open class DbPersistentMailService(
        mailTemplateContentBuilder: MailTemplateContentBuilder,
        private val mailStorageRepository: MailStorageRepository,
        mailServiceHelper: MailServiceHelper
) : AbstractMailService(mailTemplateContentBuilder, mailServiceHelper) {

    override fun sendMail(mailConfig: MailConfig): Boolean =
            mailStorageRepository.saveMailConfig(mailConfig)
}