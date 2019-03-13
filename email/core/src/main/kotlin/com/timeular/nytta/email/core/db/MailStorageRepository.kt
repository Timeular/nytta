package com.timeular.nytta.email.core.db

import com.google.common.base.Joiner
import com.timeular.nytta.email.core.MailConfig
import javax.sql.DataSource

open class MailStorageRepository(
        private val dataSource: DataSource
) {
    private val joiner = Joiner.on(",")

    companion object {
        private const val DEFAULT_ID = -1L
        private const val MAIL_STORAGE_ATTACHMENT_INSERT = """INSERT INTO $MAIL_STORAGE_ATTACHMENT_NAME(
            mail_Id, name, mime_type, data)
            VALUES (?, ?, ?, ?)"""
    }

    fun saveMailConfig(mailConfig: MailConfig): Boolean {
        dataSource.connection.use { con ->
            con.autoCommit = false
            try {
                val mailId = InsertMailPreparedStatementCreator(joiner, mailConfig).createPreparedStatement(con).use { stmt ->
                    val result = stmt.executeUpdate()
                    if (result == 1) {
                        val generatedKeys = stmt.generatedKeys
                        if (generatedKeys.next()) {
                            generatedKeys.getLong("ID")
                        } else {
                            DEFAULT_ID
                        }
                    } else {
                        DEFAULT_ID
                    }
                }

                val result = if (mailId != DEFAULT_ID) {
                    if (mailConfig.inlineAttachments.isNotEmpty()) {
                        con.prepareStatement(MAIL_STORAGE_ATTACHMENT_INSERT).use { attrStmt ->
                            val mailAttachmentParameterizedPreparedStatementSetter = MailAttachmentParameterizedPreparedStatementSetter(mailId)
                            mailConfig.inlineAttachments.forEach {
                                mailAttachmentParameterizedPreparedStatementSetter.setValues(attrStmt, it)
                            }
                            val attResult = attrStmt.executeBatch()
                            var result = true
                            attResult.forEach { amountOfResults ->
                                result = result && amountOfResults == 1
                            }
                            result
                        }
                    } else {
                        true
                    }
                } else {
                    false
                }

                con.commit()
                return result
            } catch (ex: Throwable) {
                con.rollback()
                throw ex
            }
        }
    }

    fun deleteAllMails() {
        dataSource.connection.use { con ->
            con.autoCommit = false
            try {
                con.prepareStatement("delete from $MAIL_STORAGE_ATTACHMENT_NAME").use { stmt ->
                    stmt.executeUpdate()
                }

                con.prepareStatement("delete from $MAIL_STORAGE_NAME").use { stmt ->
                    stmt.executeUpdate()
                }

                con.commit()
            } catch (ex: Throwable) {
                con.rollback()
                throw ex
            }
        }
    }
}

