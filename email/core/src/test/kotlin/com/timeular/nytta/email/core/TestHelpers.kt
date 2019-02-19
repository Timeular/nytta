package com.timeular.nytta.email.core

import org.thymeleaf.TemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun htmlTemplateResolver(): ClassLoaderTemplateResolver {
    val resolver = ClassLoaderTemplateResolver()
    resolver.prefix = "mail/templates/"
    resolver.suffix = ".html"
    resolver.templateMode = TemplateMode.HTML
    resolver.isCacheable = true
    return resolver
}

fun textTemplateResolver(): ClassLoaderTemplateResolver {
    val resolver = ClassLoaderTemplateResolver()
    resolver.prefix = "mail/templates/"
    resolver.suffix = ".txt"
    resolver.templateMode = TemplateMode.TEXT
    resolver.isCacheable = true
    return resolver
}

fun templateEngine(): TemplateEngine {
    val engine = TemplateEngine()
    engine.templateResolvers = setOf(htmlTemplateResolver(), textTemplateResolver())
    return engine
}