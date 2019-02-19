package com.timeular.nytta.email.core.db

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.timeular.nytta.email.core.DBUnitTest
import com.timeular.nytta.email.core.MailConfig
import org.dbunit.dataset.ITable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MailStorageRepositoryTest: DBUnitTest() {

    private val mailStorageRepository = MailStorageRepository(testDataSource)

    companion object {
        private const val MAIL_STORAGE_NAME = "mail_storage"
    }

    private var mailBuilder = MailConfig.Builder()

    @BeforeEach
    fun beforeEach() {
        mailBuilder = MailConfig.Builder()
                .subject("test")
                .text("text")
                .from("bla@test.com")
                .addTo("test@test.com")
    }

    @AfterEach
    fun afterEach() {
        mailStorageRepository.deleteAllMails()
    }

    @Test
    fun testSaveMailConfig() {
        mailStorageRepository.saveMailConfig(mailBuilder.build())
        assertThat(connection().getRowCount(MAIL_STORAGE_NAME), equalTo(1))

        val table = verifyTestMailTable()
        val mailId = table.getValue(0, "id") as Int
        assertThat(connection().getRowCount(MAIL_STORAGE_ATTACHMENT_NAME, "WHERE mail_id = $mailId"), equalTo(0))
    }

    @Test
    fun testSaveMailConfigWithAttachments() {
        mailBuilder.addInlineAttachment("test.txt", "text/plain", "text_test".toByteArray())
        mailBuilder.addInlineAttachment("test.html", "text/html", "html_test".toByteArray())

        mailStorageRepository.saveMailConfig(mailBuilder.build())
        val table = verifyTestMailTable()

        val mailId = table.getValue(0, "id") as Int

        val attachmentTable = connection().createQueryTable(
                MAIL_STORAGE_ATTACHMENT_NAME,
                "SELECT * FROM $MAIL_STORAGE_ATTACHMENT_NAME WHERE mail_id=$mailId"
        )

        assertThat(attachmentTable.rowCount, equalTo(2))
        assertColumnInFirstRow(attachmentTable, "name", "test.txt")
        assertColumnInFirstRow(attachmentTable, "mime_type", "text/plain")
        assertThat(attachmentTable.getValue(0, "data"), present())
    }

    private fun verifyTestMailTable(): ITable {
        assertThat(connection().getRowCount(MAIL_STORAGE_NAME), equalTo(1))
        val table = connection().createQueryTable(MAIL_STORAGE_NAME, "SELECT * FROM $MAIL_STORAGE_NAME")

        assertThat(table.rowCount, equalTo(1))
        assertColumnInFirstRow(table, "sender", mailBuilder.build().from.toString())
        assertColumnInFirstRow(table, "subject", mailBuilder.build().subject)
        assertColumnInFirstRow(table, "receiver", "test@test.com")
        assertColumnInFirstRow(table, "cc", "")
        assertColumnInFirstRow(table, "bcc", "")
        assertColumnInFirstRow(table, "rawText", mailBuilder.build().text)
        assertColumnInFirstRow(table, "html", "")
        assertColumnInFirstRow(table, "deliveryTime", null)
        assertColumnInFirstRow(table, "tag", null)

        return table
    }

    @Test
    fun testDeleteAllMails() {
        for (i in 1..10) {
            mailStorageRepository.saveMailConfig(mailBuilder.build())
        }
        assertThat(connection().getRowCount(MAIL_STORAGE_NAME), equalTo(10))

        mailStorageRepository.deleteAllMails()
        assertThat(connection().getRowCount(MAIL_STORAGE_NAME), equalTo(0))
    }
}