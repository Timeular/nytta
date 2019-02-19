package com.timeular.nytta.email.core

import com.google.common.base.Joiner

open class MailServiceHelper(
        private val isOverrideEnabled: Boolean = false,
        overrideAddressString: String = "invalid@timeular.com"
) {

    private var overrideAddress: MailContact = MailContact("invalid@timeular.com")

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

    fun modifyMailConfigBuilderForOverride(mailCfgBuilder: MailConfig.Builder): MailConfig.Builder =
            if (needToModifyMailConfigBuilder(mailCfgBuilder)) {
                doModifyMailConfigBuilder(MailConfig.Builder.from(mailCfgBuilder))
            } else {
                mailCfgBuilder
            }

    fun modifyMailConfigForOverride(mailCfg: MailConfig): MailConfig =
            if (needToModifyMailConfig(mailCfg)) {
                doModifyMailConfigBuilder(MailConfig.Builder.from(mailCfg)).build()
            } else {
                mailCfg
            }

    internal fun needToModifyMailConfigBuilder(mailCfgBuilder: MailConfig.Builder): Boolean =
            isOverrideEnabled &&
                    (mailCfgBuilder.to.size > 1 || mailCfgBuilder.to.isEmpty() || mailCfgBuilder.to[0].email != overrideAddress.email
                            || mailCfgBuilder.cc.isNotEmpty()
                            || mailCfgBuilder.bcc.isNotEmpty())

    internal fun needToModifyMailConfig(mailCfg: MailConfig): Boolean =
            isOverrideEnabled &&
                    (mailCfg.to.size > 1 || mailCfg.to.isEmpty() || mailCfg.to[0].email != overrideAddress.email
                            || mailCfg.cc.isNotEmpty()
                            || mailCfg.bcc.isNotEmpty())

    private fun doModifyMailConfigBuilder(mailCfgBuilder: MailConfig.Builder): MailConfig.Builder {
        mailCfgBuilder.clearBCC()
        mailCfgBuilder.clearCC()
        mailCfgBuilder.clearTo()
        mailCfgBuilder.addTo(overrideAddress.email, overrideAddress.name)

        return mailCfgBuilder
    }
}