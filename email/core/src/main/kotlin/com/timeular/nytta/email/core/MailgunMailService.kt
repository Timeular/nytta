package com.timeular.nytta.email.core

import com.google.common.base.Preconditions.checkNotNull
import com.google.gson.JsonParser
import okhttp3.Credentials
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

open class MailgunMailService(
        mailTemplateContentBuilder: MailTemplateContentBuilder,
        private val mailServiceHelper: MailServiceHelper,
        private val baseUrl: String,
        apiKey: String,
        private val domain: String
) : AbstractMailService(mailTemplateContentBuilder, mailServiceHelper) {

    private val okHttpClient: okhttp3.OkHttpClient
    private val jsonParser = JsonParser()

    init {
        checkNotNull(baseUrl, "You have to provide a valid base url - pls look at the mailgun site to find it.")
        checkNotNull(domain, "You have to provide a mailgun domain")
        checkNotNull(apiKey, "You have to provide the mailgun api key")

        val credential = Credentials.basic("api", apiKey)

        okHttpClient = okhttp3.OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                            .header("Authorization", credential)
                            .build()
                    chain.proceed(request)
                }
                .build()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MailgunMailService::class.java)
        private const val TIMEOUT = 60L
    }

    override fun sendMail(mailConfig: MailConfig): Boolean {
        val mailCfg = mailServiceHelper.modifyMailConfigForOverride(mailConfig)
        val body = createMultipartBody(mailCfg)
        val requestBuilder = Request.Builder()
                .url("$baseUrl$domain/messages")
                .post(body)

        okHttpClient.newCall(requestBuilder.build()).execute().use { response ->
            if (!response.isSuccessful) {
                val responseBody = jsonParser.parse(response.body()?.string() ?: "")
                logger.info(
                        "Unable to send email ({}): http status: {} - {}",
                        mailCfg.to,
                        response.code(),
                        responseBody?.asJsonObject?.get("message")?.asString ?: ""
                )
            }

            return response.isSuccessful
        }
    }

    private fun createMultipartBody(mailConfig: MailConfig): MultipartBody {
        val contentBuilder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("from", mailConfig.from.toString())
                .addFormDataPart("subject", mailConfig.subject)

        addAllContactToBuilder(contentBuilder, "to", mailConfig.to)
        addAllContactToBuilder(contentBuilder, "cc", mailConfig.cc)
        addAllContactToBuilder(contentBuilder, "bcc", mailConfig.bcc)

        if (mailConfig.text.isNotBlank()) {
            contentBuilder.addFormDataPart("text", mailConfig.text)
        }

        if (mailConfig.html.isNotBlank()) {
            contentBuilder.addFormDataPart("html", mailConfig.html)
        }

        mailConfig.deliveryTime?.let {
            val formattedDate = it.format(DateTimeFormatter.RFC_1123_DATE_TIME)

            contentBuilder.addFormDataPart("o:deliverytime", formattedDate)
                    .addFormDataPart("h:Date", formattedDate)
        }

        mailConfig.tag?.let {
            contentBuilder.addFormDataPart("o:tag", it)
        }

        mailConfig.inlineAttachments.forEach {
            val content = it.resource
            contentBuilder.addFormDataPart("inline", it.name, RequestBody.create(MediaType.parse(it.mimeType), content))
        }

        return contentBuilder.build()
    }

    private fun addAllContactToBuilder(contentBuilder: MultipartBody.Builder, field: String, contacts: List<MailContact>) =
            contacts.forEach { contentBuilder.addFormDataPart(field, it.toString()) }
}
