package com.timeular.nytta.email.spring

import com.timeular.nytta.email.core.MailServiceHelper
import com.timeular.nytta.email.core.MailTemplateContentBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.TemplateEngine
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ITemplateResolver

@Configuration
open class SpringMailConfiguration {
    @Bean
    open fun mailTemplateContentBuilder(
            mailTemplateEngine: TemplateEngine
    ): MailTemplateContentBuilder = MailTemplateContentBuilder(mailTemplateEngine)

    @Bean
    open fun mailServiceHelper(
            @Value("\${mail.test.override.enabled}")
            isOverrideEnabled: Boolean,
            @Value("\${mail.test.override.receiver}")
            overrideAddressString: String
    ): MailServiceHelper = MailServiceHelper(isOverrideEnabled, overrideAddressString)

    @Bean
    open fun htmlTemplateResolver(
            applicationContext: ApplicationContext
    ): ITemplateResolver {
        val templateResolver = SpringResourceTemplateResolver()
        templateResolver.setApplicationContext(applicationContext)
        templateResolver.prefix = "classpath:/mail/templates/"
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.isCacheable = true
        return templateResolver
    }

    @Bean
    open fun txtTemplateResolver(
            applicationContext: ApplicationContext
    ): ITemplateResolver {
        val templateResolver = SpringResourceTemplateResolver()
        templateResolver.setApplicationContext(applicationContext)
        templateResolver.prefix = "classpath:/mail/templates/"
        templateResolver.suffix = ".txt"
        templateResolver.templateMode = TemplateMode.TEXT
        templateResolver.isCacheable = true
        return templateResolver
    }

    @Bean
    open fun mailTemplateEngine(
            htmlTemplateResolver: ITemplateResolver,
            txtTemplateResolver: ITemplateResolver
    ): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.templateResolvers = setOf(htmlTemplateResolver, txtTemplateResolver)
        templateEngine.enableSpringELCompiler = true
        return templateEngine
    }
}