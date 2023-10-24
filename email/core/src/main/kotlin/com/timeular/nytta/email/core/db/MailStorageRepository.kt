package com.timeular.nytta.email.core.db

import com.timeular.nytta.email.core.MailConfig
import java.sql.Connection
import javax.sql.DataSource

open class MailStorageRepository(
    private val dataSource: DataSource
) {
    companion object {
        private const val BATCH_SIZE = 250
        private const val DEFAULT_ID = -1L
        private const val MAIL_STORAGE_ATTACHMENT_INSERT = """INSERT INTO $MAIL_STORAGE_ATTACHMENT_NAME(
            mail_Id, name, mime_type, data)
            VALUES (?, ?, ?, ?)"""
    }

    fun saveMailConfig(mailConfig: MailConfig): Boolean =
        executeInTransaction { con ->
            val mailId = InsertMailPreparedStatementCreator(mailConfig).createPreparedStatement(con).use { stmt ->
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

            if (mailId != DEFAULT_ID) {
                if (mailConfig.inlineAttachments.isNotEmpty()) {
                    con.prepareStatement(MAIL_STORAGE_ATTACHMENT_INSERT).use { attrStmt ->
                        val mailAttachmentParameterizedPreparedStatementSetter =
                            MailAttachmentParameterizedPreparedStatementSetter(mailId)
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
        }

    fun deleteAllMails() {
        executeInTransaction { con ->
            con.prepareStatement("DELETE FROM $MAIL_STORAGE_ATTACHMENT_NAME").use { stmt ->
                stmt.executeUpdate()
            }

            con.prepareStatement("DELETE FROM $MAIL_STORAGE_NAME").use { stmt ->
                stmt.executeUpdate()
            }
        }
    }

    fun deleteMailsByTag(tag: String) {
        val ids = dataSource.connection.use { con ->
            con.prepareStatement("SELECT id FROM $MAIL_STORAGE_NAME WHERE tag=?").use { stmt ->
                stmt.setString(1, tag)
                stmt.executeQuery().use { rs ->
                    val result = mutableListOf<Long>()
                    while (rs.next()) {
                        result.add(rs.getLong("id"))
                    }
                    result
                }
            }
        }

        if (ids.isNotEmpty()) {
            ids.chunked(BATCH_SIZE).forEach { idList ->
                executeInTransaction { con ->
                    val marker = idList.joinToString(",") { "?" }
                    con.prepareStatement("DELETE FROM $MAIL_STORAGE_ATTACHMENT_NAME WHERE mail_Id IN ($marker)")
                        .use { stmt ->
                            for (i in 1..idList.size) {
                                stmt.setLong(i, idList[i - 1])
                            }
                            stmt.executeUpdate()
                        }

                    con.prepareStatement("DELETE FROM $MAIL_STORAGE_NAME WHERE id IN ($marker)").use { stmt ->
                        for (i in 1..idList.size) {
                            stmt.setLong(i, idList[i - 1])
                        }
                        stmt.executeUpdate()
                    }
                }
            }
        }
    }

    private fun <T> executeInTransaction(applyFn: (Connection) -> T): T {
        dataSource.connection.use { con ->
            con.autoCommit = false
            try {
                val res = applyFn(con)

                con.commit()
                return res
            } catch (ex: Throwable) {
                con.rollback()
                throw ex
            } finally {
                con.autoCommit = true
            }
        }
    }
}

