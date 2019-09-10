package com.timeular.nytta.email.core

import com.google.common.base.Joiner
import java.util.regex.Pattern

open class MailServiceHelper @JvmOverloads constructor(
        private val isOverrideEnabled: Boolean = false,
        overrideAddressString: String = "invalid@timeular.com",
        excludePattern: String? = null
) {

    private var overrideAddress: MailContact = MailContact("invalid@timeular.com")

    private val compiledExcludePattern: Pattern? = excludePattern?.let { Pattern.compile(it) }

    init {
        overrideAddress = MailContact(overrideAddressString)
    }

    private val joiner = Joiner.on(",")

    fun modifyTemplateContextForOverride(mailContext: Map<String, Any>, mailCfgBuilder: MailConfig.Builder): Map<String, Any> =
            if (isOverrideEnabled) {
                val resultMap = HashMap(mailContext)
                resultMap["overrideAddress"] = overrideAddress.toString()
                resultMap["originalAddressTo"] = joiner.join(mailCfgBuilder.to)
                resultMap["originalAddressCC"] = joiner.join(mailCfgBuilder.cc)
                resultMap["originalAddressBcc"] = joiner.join(mailCfgBuilder.bcc)

                resultMap
            } else {
                mailContext
            }

    fun modifyMailConfigBuilder(mailCfgBuilder: MailConfig.Builder): MailConfig.Builder =
            if (mailCfgBuilder.isNoEmailProvided()) {
                mailCfgBuilder
            } else {
                doModifyMailConfigBuilder(MailConfig.Builder.from(mailCfgBuilder))
            }

    fun modifyMailConfig(mailCfg: MailConfig): MailConfig =
            if (mailCfg.isNoEmailProvided()) {
                mailCfg
            } else {
                val builder = doModifyMailConfigBuilder(MailConfig.Builder.from(mailCfg))
                if (builder.isNoEmailProvided()) {
                    MailConfig.EMPTY_MAIL_CONFIG
                } else {
                    builder.build()
                }
            }

    private fun doModifyMailConfigBuilder(mailCfgBuilder: MailConfig.Builder): MailConfig.Builder {
        if (needToModifyMailConfigBuilder(mailCfgBuilder)) {
            doModifyMailConfigBuilderForOverride(mailCfgBuilder)
        }

        compiledExcludePattern?.run {
            val to = mailCfgBuilder.to
            val cc = mailCfgBuilder.cc
            val bcc = mailCfgBuilder.bcc

            mailCfgBuilder.clearTo()
                    .clearCC()
                    .clearBCC()
                    .addTo(*to.filterNot { compiledExcludePattern.matcher(it.email).matches() }.toTypedArray())
                    .addCC(*cc.filterNot { compiledExcludePattern.matcher(it.email).matches() }.toTypedArray())
                    .addBCC(*bcc.filterNot { compiledExcludePattern.matcher(it.email).matches() }.toTypedArray())
        }

        return if (mailCfgBuilder.isNoEmailProvided()) {
            MailConfig.Builder.EMPTY_MAIL_BUILDER
        } else {
            mailCfgBuilder
        }
    }

    internal fun needToModifyMailConfigBuilder(mailCfgBuilder: MailConfig.Builder): Boolean =
            isOverrideEnabled &&
                    (mailCfgBuilder.to.size > 1 || mailCfgBuilder.to.isEmpty() || mailCfgBuilder.to[0].email != overrideAddress.email
                            || mailCfgBuilder.cc.isNotEmpty()
                            || mailCfgBuilder.bcc.isNotEmpty())

    private fun doModifyMailConfigBuilderForOverride(mailCfgBuilder: MailConfig.Builder): MailConfig.Builder {
        if (isOverrideEnabled) {
            mailCfgBuilder.clearBCC()
            mailCfgBuilder.clearCC()
            mailCfgBuilder.clearTo()
            mailCfgBuilder.addTo(overrideAddress.email, overrideAddress.name)
        }

        return mailCfgBuilder
    }
}