package com.timeular.nytta.email.core.db

import com.timeular.nytta.email.core.Attachment
import com.timeular.nytta.email.core.MailConfig
import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.Statement
import java.time.ZoneOffset
import java.time.ZonedDateTime

const val MAIL_STORAGE_NAME = "mail_storage"
const val MAIL_STORAGE_ATTACHMENT_NAME = "mail_storage_attachment"

class InsertMailPreparedStatementCreator(
    private val mailConfig: MailConfig
) {

    companion object {
        private const val MAIL_STORAGE_INSERT = """INSERT INTO $MAIL_STORAGE_NAME(
            sender, subject, receiver, cc, bcc, rawText, html, deliveryTime, tag)
            VALUES (?, ?, ?, ?, ?, ? , ?, ? , ?)"""
    }

    fun createPreparedStatement(con: Connection?): PreparedStatement {
        val stmt = con!!.prepareStatement(MAIL_STORAGE_INSERT, Statement.RETURN_GENERATED_KEYS)

        stmt.setString(1, mailConfig.from.toString())
        stmt.setString(2, mailConfig.subject)
        stmt.setString(3, mailConfig.to.joinToString(",") { it.toString() })
        stmt.setString(4, mailConfig.cc.joinToString(",") { it.toString() })
        stmt.setString(5, mailConfig.bcc.joinToString(",") { it.toString() })
        stmt.setString(6, mailConfig.text)
        stmt.setString(7, mailConfig.html)
        stmt.setDate(8, mailConfig.deliveryTime?.toSqlDate())
        stmt.setString(9, mailConfig.tag)

        return stmt
    }

    private fun ZonedDateTime.toSqlDate(): Date =
        Date(this.withZoneSameInstant(ZoneOffset.UTC).toInstant().toEpochMilli())
}

class MailAttachmentParameterizedPreparedStatementSetter(
    private val mailId: Long
) {

    fun setValues(ps: PreparedStatement, attachment: Attachment) {
        ps.setLong(1, mailId)
        ps.setString(2, attachment.name)
        ps.setString(3, attachment.mimeType)

        attachment.resource.inputStream().use {
            ps.setBinaryStream(4, it)
        }
        ps.addBatch()
    }
}