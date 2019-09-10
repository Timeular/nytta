package com.timeular.nytta.email.core

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.sameInstance
import org.junit.jupiter.api.Test

internal class MailServiceHelperTest {

    companion object {
        private const val MAIL_TO_ADDRESS = "test@timeular.com"
    }

    @Test
    fun testModifyTemplateContextForOverride() {
        var helper = MailServiceHelper(false, MAIL_TO_ADDRESS)

        val builder = MailConfig.Builder()
                .subject("Blub")
                .html("blab")
                .addBCC("test@mail.com")
                .addBCC("test2@mail.com")
                .addTo("another@mail.com")

        val ctx: Map<String, Any> = HashMap()

        var result = helper.modifyTemplateContextForOverride(ctx, builder)
        assertThat(result, sameInstance(ctx))

        helper = MailServiceHelper(true, MAIL_TO_ADDRESS)
        result = helper.modifyTemplateContextForOverride(ctx, builder)

        assertThat(result.size, equalTo(4))
        assertThat(result["overrideAddress"] as String?, equalTo(MAIL_TO_ADDRESS))
        assertThat(result["originalAddressTo"] as String?, equalTo("another@mail.com"))
        assertThat(result["originalAddressCC"] as String?, equalTo(""))
        assertThat(result["originalAddressBcc"] as String?, equalTo("test@mail.com,test2@mail.com"))
    }

    @Test
    fun testModifyMailConfigBuilderForOverride() {
        var helper = MailServiceHelper(false, MAIL_TO_ADDRESS)

        val builder = MailConfig.Builder()
                .subject("Blub")
                .html("blab")
                .addBCC("test@mail.com")
                .addBCC("test2@mail.com")
                .addTo("another@mail.com")

        var result = helper.modifyMailConfigBuilder(builder)
        assertThat(result.bcc, hasSize(equalTo(2)))
        assertThat(result.bcc[0], equalTo(builder.bcc[0]))
        assertThat(result.bcc[1], equalTo(builder.bcc[1]))
        assertThat(result.cc, hasSize(equalTo(0)))
        assertThat(result.to.size, equalTo(1))
        assertThat(result.to[0].email, equalTo(builder.to[0].email))

        helper = MailServiceHelper(true, MAIL_TO_ADDRESS)
        result = helper.modifyMailConfigBuilder(builder)

        assertThat(result.bcc.isEmpty(), equalTo(true))
        assertThat(result.cc.isEmpty(), equalTo(true))
        assertThat(result.to.size, equalTo(1))
        assertThat(result.to[0].email, equalTo(MAIL_TO_ADDRESS))
    }

    @Test
    fun testModifyMailConfigForOverride() {
        val mailCfg = MailConfig.Builder()
                .subject("Blub")
                .html("blab")
                .from("support@timeular.com")
                .addBCC("test@mail.com")
                .addBCC("test2@mail.com")
                .addTo("another@mail.com")
                .build()

        var helper = MailServiceHelper(false, MAIL_TO_ADDRESS)
        var result = helper.modifyMailConfig(mailCfg)
        assertThat(result.to[0].email, equalTo(mailCfg.to[0].email))
        assertThat(result.bcc[0].email, equalTo(mailCfg.bcc[0].email))
        assertThat(result.bcc[1].email, equalTo(mailCfg.bcc[1].email))

        helper = MailServiceHelper(true, MAIL_TO_ADDRESS)
        result = helper.modifyMailConfig(mailCfg)

        assertThat(result.bcc.isEmpty(), equalTo(true))
        assertThat(result.cc.isEmpty(), equalTo(true))
        assertThat(result.to.size, equalTo(1))
        assertThat(result.to[0].email, equalTo(MAIL_TO_ADDRESS))
    }

    @Test
    fun testModifyMailConfigExcludeEmailAddress() {
        val mailCfg = MailConfig.Builder()
                .subject("Blub")
                .html("blab")
                .from("support@timeular.com")
                .addCC("test@mail.com")
                .addBCC("test2@mail.com")
                .addTo("another@mail.com", "some@mail.com")
                .build()

        var helper = MailServiceHelper( excludePattern = "some@mail.com" )
        var result = helper.modifyMailConfig(mailCfg)
        assertThat(result.to, hasSize(equalTo(1)))
        assertThat(result.to[0].email, equalTo("another@mail.com"))
        assertThat(result.cc[0].email, equalTo(mailCfg.cc[0].email))
        assertThat(result.bcc[0].email, equalTo(mailCfg.bcc[0].email))

        helper = MailServiceHelper(excludePattern = ".*")
        result = helper.modifyMailConfig(mailCfg)

        assertThat(result.isNoEmailProvided(), equalTo(true))
    }

    @Test
    fun testNeedToModifyMailConfigBuilder() {
        var helper = MailServiceHelper(false, MAIL_TO_ADDRESS)
        val builder = MailConfig.Builder()
                .subject("Blub")
                .html("blab")
                .addBCC("test@mail.com")

        assertThat(helper.needToModifyMailConfigBuilder(builder), equalTo(false))

        helper = MailServiceHelper(true, MAIL_TO_ADDRESS)
        assertThat(helper.needToModifyMailConfigBuilder(builder), equalTo(true))

        builder.clearBCC()
        builder.addCC("test@mail.com")
        assertThat(helper.needToModifyMailConfigBuilder(builder), equalTo(true))
        builder.clearCC()
        builder.addTo("test@mail.com")
        assertThat(helper.needToModifyMailConfigBuilder(builder), equalTo(true))

        builder.clearTo()
        assertThat(helper.needToModifyMailConfigBuilder(builder), equalTo(true))

        builder.addTo(MAIL_TO_ADDRESS)
        assertThat(helper.needToModifyMailConfigBuilder(builder), equalTo(false))

        builder.addCC("test@mailc.om")
        assertThat(helper.needToModifyMailConfigBuilder(builder), equalTo(true))

        builder.clearCC()
        builder.addBCC("test@mailc.om")
        assertThat(helper.needToModifyMailConfigBuilder(builder), equalTo(true))
    }
}