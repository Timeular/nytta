package com.timeular.nytta.email.core.db

import com.google.common.base.Joiner
import com.timeular.nytta.email.core.MailConfig
import com.timeular.nytta.email.core.MailException
import javax.sql.DataSource

open class MailStorageRepository(
        private val dataSource: DataSource
) {
    private val joiner = Joiner.on(",")

    companion object {
        private const val MAIL_STORAGE_ATTACHMENT_INSERT = """INSERT INTO $MAIL_STORAGE_ATTACHMENT_NAME(
            mail_Id, name, mime_type, data)
            VALUES (?, ?, ?, ?)"""
    }

    fun saveMailConfig(mailConfig: MailConfig): Boolean {
        val con = dataSource.connection
        val stmt = InsertMailPreparedStatementCreator(joiner, mailConfig).createPreparedStatement(con)
        val attrStmt = con.prepareStatement(MAIL_STORAGE_ATTACHMENT_INSERT)
        try {
            var result = stmt.executeUpdate() == 1
            return if (result && mailConfig.inlineAttachments.isNotEmpty()) {
                var mailId = -1L
                val generatedKeys = stmt.generatedKeys
                if (generatedKeys.next()) {
                    mailId =  generatedKeys.getLong("ID")
                }
                con.commit()

                if (mailId > 0) {
                    val mailAttachmentParameterizedPreparedStatementSetter = MailAttachmentParameterizedPreparedStatementSetter(mailId)
                    mailConfig.inlineAttachments.forEach {
                        mailAttachmentParameterizedPreparedStatementSetter.setValues(attrStmt, it)
                    }
                    val attResult = attrStmt.executeBatch()
                    con.commit()

                    attResult.forEach {amountOfResults ->
                        result = result && amountOfResults == 1
                    }
                } else {
                    throw MailException("Unable to save mail attachments - unable to fetch id of stored mail")
                }
                        stmt.close()
                result
            } else {
                result
            }
        } finally {
            attrStmt.close()
            stmt.close()
        }
    }

    fun deleteAllMails() {
        val con = dataSource.connection
        val attachmentStmt = con.prepareStatement("delete from $MAIL_STORAGE_ATTACHMENT_NAME")
        val mailStmt = con.prepareStatement("delete from $MAIL_STORAGE_NAME")
        try {
            attachmentStmt.executeUpdate()
            mailStmt.executeUpdate()
            con.commit()
        } finally {
            attachmentStmt.close()
            mailStmt.close()
        }
    }
}

