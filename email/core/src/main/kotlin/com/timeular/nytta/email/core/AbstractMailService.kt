package com.timeular.nytta.email.core

import org.thymeleaf.exceptions.TemplateInputException
import java.io.FileNotFoundException
import java.time.ZonedDateTime
import java.util.*

abstract class AbstractMailService(
        private val mailTemplateContentBuilder: MailTemplateContentBuilder,
        private val mailServiceHelper: MailServiceHelper
) : MailService {

    override fun sendMail(
            mailTemplate: MailTemplate,
            mailContext: Map<String, Any>,
            receiver: Set<MailContact>,
            deliveryTime: ZonedDateTime?,
            inlineAttachments: List<Attachment>,
            locale: Locale
    ): Boolean {

        var mailCfgBuilder = MailConfig.Builder.from(mailTemplate.mailConfigBuilder)

        receiver.forEach {
            mailCfgBuilder.addTo(it.email, it.name)
        }

        val correctedCtx = mailServiceHelper.modifyTemplateContextForOverride(mailContext, mailCfgBuilder)

        mailCfgBuilder = mailServiceHelper.modifyMailConfigBuilder(mailCfgBuilder)

        if (mailCfgBuilder.isNoEmailProvided()) {
            return false
        }

        mailCfgBuilder.deliveryTime(deliveryTime)

        mailTemplate.htmlTemplate?.run {
            resolveContent(mailTemplate.htmlTemplate, correctedCtx, locale)?.let { content ->
                mailCfgBuilder.html(content)
            }
        }

        mailTemplate.txtTemplate?.run {
            resolveContent(mailTemplate.txtTemplate, correctedCtx, locale)?.let { content ->
                mailCfgBuilder.text(content)
            }
        }

        inlineAttachments.forEach {
            mailCfgBuilder.addInlineAttachment(it)
        }
        mailCfgBuilder.locale = locale

        return sendMail(mailCfgBuilder.build())
    }

    fun resolveContent(baseName: String, ctx: Map<String, Any>, locale: Locale): String? {
        resolveTemplateNames(baseName, locale).forEach { template ->
            try {
                return mailTemplateContentBuilder.build(template, ctx, locale)
            } catch (ex: TemplateInputException) {
                if (ex.cause !is FileNotFoundException) {
                    throw ex
                }
            }
        }

        return null
    }

    fun resolveTemplateNames(name: String, locale: Locale): List<String> {
        val res = ArrayList<String>(4)
        val baseName = name.substringBeforeLast(".")
        val ext = name.substringAfterLast(".")

        res.add(name)
        val tmp = StringBuilder(baseName)

        val lang = locale.language
        if (lang.isNotBlank()) {
            tmp.append("_").append(lang)
            res.add(0, "${tmp}.$ext")
        }

        val country = locale.country
        if (country.isNotBlank()) {
            tmp.append("_").append(country)
            res.add(0, "${tmp}.$ext")
        }

        val variant = locale.variant
        if (variant.isNotBlank() && (lang.isNotBlank() || country.isNotBlank())) {
            tmp.append("_").append(variant)
            res.add(0, "$tmp.$ext")
        }

        return res
    }
}