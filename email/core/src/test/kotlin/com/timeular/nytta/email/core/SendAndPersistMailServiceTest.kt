package com.timeular.nytta.email.core

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

internal class SendAndPersistMailServiceTest {

    private val mailBuilder = MailConfig.Builder()
            .subject("test")
            .text("text")
            .from("bla@test.com")
            .addTo("test@test.com")

    @Test
    fun testSendMailWithConfig() {
        doTestSendMailWithConfig(
                sendMails = true,
                returnSend = true,
                persistMails = true,
                returnPersist = true,
                expectedResult = true
        )

        doTestSendMailWithConfig(
                sendMails = false,
                persistMails = true,
                returnPersist = true,
                expectedResult = true
        )

        doTestSendMailWithConfig(
                sendMails = false,
                persistMails = false,
                expectedResult = false
        )
    }

    @Test
    fun testSendMailWithTemplate() {
        doTestSendMailWithTemplate(
                sendMails = true,
                returnSend = true,
                persistMails = true,
                returnPersist = true,
                expectedResult = true
        )

        doTestSendMailWithTemplate(
                sendMails = false,
                persistMails = true,
                returnPersist = true,
                expectedResult = true
        )

        doTestSendMailWithTemplate(
                sendMails = false,
                persistMails = false,
                expectedResult = false
        )
    }

    private fun doTestSendMailWithConfig(
            persistMails: Boolean,
            sendMails: Boolean,
            returnPersist: Boolean = false,
            returnSend: Boolean = false,
            expectedResult: Boolean
    ) {
        assertThat(createMailService(
                persistMails = persistMails,
                sendMails = sendMails,
                returnPersist = returnPersist,
                returnSend = returnSend
        ).sendMail(mailBuilder.build()),
                equalTo(expectedResult)
        )
    }

    private fun doTestSendMailWithTemplate(
            persistMails: Boolean,
            sendMails: Boolean,
            returnPersist: Boolean = false,
            returnSend: Boolean = false,
            expectedResult: Boolean
    ) {
        assertThat(createMailService(
                persistMails = persistMails,
                sendMails = sendMails,
                returnPersist = returnPersist,
                returnSend = returnSend
        ).sendMail(
                mailTemplate = MailTemplate(
                        mailConfigBuilder = mailBuilder,
                        htmlTemplate = "",
                        txtTemplate = ""
                ),
                mailContext = HashMap(),
                receiver = HashSet()
        ),
                equalTo(expectedResult)
        )
    }

    private fun createMailService(
            persistMails: Boolean,
            sendMails: Boolean,
            returnPersist: Boolean,
            returnSend: Boolean
    ) =
            SendAndPersistMailService(
                    dbPersistentMailService = MockDbPersistentMailService(returnPersist),
                    mailgunMailService = MockMailGunMailService(returnSend),
                    persistMails = persistMails,
                    sendMails = sendMails
            )
}

open class MockDbPersistentMailService(
        val returnValue: Boolean
) : DbPersistentMailService(
        mailTemplateContentBuilder = mock(),
        mailStorageRepository = mock(),
        mailServiceHelper = mock()
) {
    override fun sendMail(mailConfig: MailConfig): Boolean = returnValue

    override fun sendMail(mailTemplate: MailTemplate, mailContext: Map<String, Any>, receiver: Set<MailContact>, deliveryTime: ZonedDateTime?, inlineAttachments: List<Attachment>): Boolean = returnValue
}

open class MockMailGunMailService(
        val returnValue: Boolean
) : MailgunMailService(
        mailTemplateContentBuilder = mock(),
        mailServiceHelper = mock(),
        apiKey = "A Key",
        baseUrl = "invalid url",
        domain = "domain"
) {
    override fun sendMail(mailConfig: MailConfig): Boolean = returnValue

    override fun sendMail(mailTemplate: MailTemplate, mailContext: Map<String, Any>, receiver: Set<MailContact>, deliveryTime: ZonedDateTime?, inlineAttachments: List<Attachment>): Boolean = returnValue
}