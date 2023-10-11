package com.timeular.nytta.email.core

import java.time.ZonedDateTime
import java.util.*

data class MailContact @JvmOverloads constructor(
    val email: String,
    val name: String? = null
) {
    override fun toString(): String =
        name?.let { "$name <$email>" } ?: email
}

data class Attachment(
    val name: String,
    val mimeType: String,
    val resource: ByteArray
)

private val DEFAULT_LOCALE: Locale = Locale("default_string")

class MailConfig private constructor(builder: Builder, locale: Locale? = null) {

    companion object {
        @JvmStatic
        fun newBuilder() = Builder()

        @JvmStatic
        val EMPTY_MAIL_CONFIG = MailConfig(Builder.EMPTY_MAIL_BUILDER)
    }

    val from: MailContact
    val subject: String
    val to: List<MailContact>
    val cc: List<MailContact>
    val bcc: List<MailContact>
    val text: String
    val html: String
    val deliveryTime: ZonedDateTime?
    val tag: String?
    val inlineAttachments: List<Attachment>

    init {
        from = builder.from as MailContact
        subject = resolveSubject(builder.subjects, locale)
        to = ArrayList(builder.to)
        cc = ArrayList(builder.cc)
        bcc = ArrayList(builder.bcc)
        text = builder.text
        html = builder.html
        deliveryTime = builder.deliveryTime
        tag = builder.tag
        inlineAttachments = builder.inlineAttachments
    }

    fun isNoEmailProvided(): Boolean =
        to.isEmpty() && cc.isEmpty() && bcc.isEmpty()

    class Builder {

        companion object {
            @JvmStatic
            fun from(builder: Builder): Builder {
                val newBuilder = Builder()
                newBuilder.from = builder.from
                newBuilder.subjects += builder.subjects
                newBuilder.to += builder.to
                newBuilder.cc += builder.cc
                newBuilder.bcc += builder.bcc
                newBuilder.text = builder.text
                newBuilder.html = builder.html
                newBuilder.deliveryTime = builder.deliveryTime
                newBuilder.tag = builder.tag
                newBuilder.inlineAttachments += builder.inlineAttachments

                return newBuilder
            }

            @JvmStatic
            fun from(mailCfg: MailConfig): Builder {
                val builder = Builder()
                builder.from = mailCfg.from
                builder.subjects[DEFAULT_LOCALE] = mailCfg.subject
                builder.to += mailCfg.to
                builder.cc += mailCfg.cc
                builder.bcc += mailCfg.bcc
                builder.text = mailCfg.text
                builder.html = mailCfg.html
                builder.deliveryTime = mailCfg.deliveryTime
                builder.tag = mailCfg.tag
                builder.inlineAttachments += mailCfg.inlineAttachments

                return builder
            }

            @JvmStatic
            val EMPTY_MAIL_BUILDER = Builder().from("nobody@here.me")
        }

        internal var from: MailContact? = null
        internal var subjects: MutableMap<Locale, String> = mutableMapOf(DEFAULT_LOCALE to "")
        internal var to: List<MailContact> = ArrayList()
        internal var cc: List<MailContact> = ArrayList()
        internal var bcc: List<MailContact> = ArrayList()
        internal var text: String = ""
        internal var html: String = ""
        internal var inlineAttachments: List<Attachment> = ArrayList()
        internal var deliveryTime: ZonedDateTime? = null
        internal var tag: String? = null

        fun from(mailContact: MailContact): Builder {
            this.from = mailContact
            return this
        }

        fun from(email: String, name: String? = null): Builder {
            this.from = MailContact(email, name)
            return this
        }

        @JvmOverloads
        fun subject(subject: String, locale: Locale? = null): Builder {
            val key = locale ?: DEFAULT_LOCALE
            this.subjects[key] = subject
            return this
        }

        fun addTo(email: String, name: String? = null): Builder {
            this.to += MailContact(email, name)
            return this
        }

        fun addTo(vararg emails: MailContact): Builder {
            this.to += emails
            return this
        }

        fun addCC(email: String, name: String? = null): Builder {
            this.cc += MailContact(email, name)
            return this
        }

        fun addCC(vararg emails: MailContact): Builder {
            this.cc += emails
            return this
        }

        fun addBCC(email: String, name: String? = null): Builder {
            this.bcc += MailContact(email, name)
            return this
        }

        fun addBCC(vararg emails: MailContact): Builder {
            this.bcc += emails
            return this
        }

        fun text(text: String): Builder {
            this.text = text
            return this
        }

        fun html(html: String): Builder {
            this.html = html
            return this
        }

        fun clearTo(): Builder {
            this.to = ArrayList()
            return this
        }

        fun clearCC(): Builder {
            this.cc = ArrayList()
            return this
        }

        fun clearBCC(): Builder {
            this.bcc = ArrayList()
            return this
        }

        fun tag(tag: String): Builder {
            this.tag = tag
            return this
        }

        fun deliveryTime(deliveryTime: ZonedDateTime?): Builder {
            this.deliveryTime = deliveryTime
            return this
        }

        fun addInlineAttachment(attachment: Attachment): Builder {
            this.inlineAttachments += attachment
            return this
        }

        fun addInlineAttachment(
            name: String,
            mimeType: String,
            resource: ByteArray
        ): Builder {
            this.addInlineAttachment(
                Attachment(
                    mimeType = mimeType,
                    name = name,
                    resource = resource
                )
            )
            return this
        }

        fun isNoEmailProvided(): Boolean =
            to.isEmpty() && cc.isEmpty() && bcc.isEmpty()

        @JvmOverloads
        fun build(locale: Locale? = null): MailConfig {
            if (from == null)
                throw MailConfigurationException("The From field is missing")

            if (subjects.isEmpty() || (subjects.size == 1 && subjects[DEFAULT_LOCALE]?.isBlank() == true))
                throw MailConfigurationException("Subject is not allowed to be blank")

            if (isNoEmailProvided())
                throw MailConfigurationException("You have to at least configure one of the following: TO, CC, BCC")

            if (text.isBlank() && html.isBlank())
                throw MailConfigurationException("Sending an email with an empty body is not supported at the moment")

            return MailConfig(this, locale)
        }
    }

    private fun resolveSubject(subjects: Map<Locale, String>, locale: Locale?): String =
        locale?.let {
            val list = mutableListOf(DEFAULT_LOCALE)
            val lang = locale.language

            if (lang.isNotBlank()) {
                list.add(0, Locale(lang))
            }

            val country = locale.country
            if (country.isNotBlank()) {
                list.add(0, Locale(lang, country))
            }

            val variant = locale.variant
            if (variant.isNotBlank() && (lang.isNotBlank() || country.isNotBlank())) {
                list.add(0, Locale(lang, country, variant))
            }

            list.forEach {
                val sub = subjects[it]
                if (sub != null) {
                    return sub
                }
            }

            ""
        } ?: subjects[DEFAULT_LOCALE] ?: ""
}

data class MailTemplate @JvmOverloads constructor(
    val mailConfigBuilder: MailConfig.Builder,
    val htmlTemplate: String? = null,
    val txtTemplate: String? = null
)
