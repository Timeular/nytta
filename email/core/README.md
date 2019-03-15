# Nytta email core

The Nytta email module provides helpers and services for handling e-mails in web-services.

## Latest release

The most recent release is email-core 2.2.0, released March 14, 2019.

### Maven

```xml
<dependency>
  <groupId>com.timeular.nytta.email</groupId>
  <artifactId>core</artifactId>
  <version>2.2.0</version>
</dependency>
```

### Gradle

```gradle
// Gradle Groovy DSL
compile 'com.timeular.nytta.email:core:2.2.0'

// Gradle Kotlin DSL
compile(group = "com.timeular.nytta.email", name = "core", version = "2.2.0")
```

### Manual

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta.email/core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta.email/core/badge.svg)
Download the latest release from the maven central repository and add the jar file to you classpath.

## Requirements

nytta email core is compiled against JDK8+ and has the following required dependencies:

- thymeleaf
- okhttp3
- gson
- guava
- slf4j

## Components

There are several implementations for e-mails in this package.

- `StringMailService` - Generates e-mails and saves the result in a thread-local variable
- `MailgunMailService` - Sends e-mails via `mailgun`
- `DbPersistentMailService` - Persists e-mails in the database
- `SendAndPersistMailService` - Combines the above two services (mailgun + db)
- `AsyncMailService` - Makes one MailService async callable

All of them are based on the `AbstractMailservice` and implement the `MailService` interface.

There are also several helpers and domain objects.

### Usage

In order to use this package, you need to create one of the above mentioned services, or create your own service.

For example, configuring the `MailgunMailService` looks like this:

First, configure the template resolvers and the thymeleaf TemplateEngine:

```kotlin
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
```

Create the `MailServiceHelper`:

```kotlin
val isOverrideEnabled = false // override all receivers
val overrideAddressString = "" // mail address the receivers will be overridden with 
val mailServiceHelper = MailServiceHelper(isOverrideEnabled, overrideAddressString)
```

Create the `MailTemplateContentBuilder`:

```kotlin
val mailTemplateContentBuilder = MailTemplateContentBuilder(templateEngine)
```

Create the `MailgunMailService`:

```kotlin
val baseUrl = "" // the mailgun base URL
val apiKey = "" // the mailgun api key
val domain = "" // the mailgun domain
val mailgunMailService = MailgunMailService(mailTemplateContentBuilder, mailServiceHelper, baseUrl, apiKey, domain)
```

Create a `MailConfig`:

```kotlin
val mailConfig = MailConfig.Builder()
                .from("hi@example.com", "Hi")
                .subject("Hello Test")
                .text("Yeah! Emails are sending!")
                .addTo("receiver@example.com")
                .build()
```

Send an e-mail:

```kotlin
val result = mailgunMailService.sendMail(mailConfig)
```

To reuse one `MailConfig` a so called `MailTemplate can be used:

```kotlin
    val mailConfigBuilder = MailConfig.Builder()
            .from("hi@example.com", "Hi")
            .subject("Hello Test")

    return MailTemplate(
            mailConfigBuilder = mailConfigBuilder,
            htmlTemplate = "emailTemplate.html",
            txtTemplate = "emailTemplate.txt"
    )
```

Send an email via an email template (the email will be loaded from the defined thymeleaf resolver
path):

```kotlin
val result = mailgunMailService.sendMail(
                        mailTemplate = tmpl,
                        mailContext = mapOf("user" to "John Doe"),
                        receiver = setOf(MailContact("john.doe@domain.com")),
                        deliveryTime = ZonedDateTime.parse("2018-08-12T10:00:00+02:00")
                )
```

#### Spring

For the spring config, please take a look at [spring-email](https://github.com/Timeular/nytta/tree/master/spring-email)

## License

The nytta email module is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
