# Nytta spring-email

The Nytta spring-email module provides spring configuration for the [nytta email](https://github.com/Timeular/nytta/tree/master/email) module.

## Latest release

The most recent release is spring-email 2.2.0, released March 14, 2019.

### Maven

```xml
<dependency>
  <groupId>com.timeular.nytta.email</groupId>
  <artifactId>spring</artifactId>
  <version>2.2.0</version>
</dependency>
```

### Gradle

```gradle
// Gradle Groovy DSL
compile 'com.timeular.nytta.email:spring:2.2.0'

// Gradle Kotlin DSL
compile(group = "com.timeular.nytta.email", name = "spring", version = "2.2.0")
```

### Manual

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta.email/spring/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta.email/spring/badge.svg)
Download the latest release from the maven central repository and add the jar file to you classpath.

## Requirements

nytta spring-email is compiled against JDK8+ and has the following required dependencies:

- nytta email-core
  - thymeleaf
  - okhttp3
  - gson
  - guava
  - slf4j
- spring-context
- thymeleaf-spring5

## Usage

You can use this configuration by importing it into your Spring project, e.g.:

```kotlin
import com.timeular.nytta.email.spring.SpringMailConfiguration

@Import(
        SpringMailConfiguration::class
)
class MyApplication

```

### Configuration

In order to configure the different components of the email module, you can simply create and register the beans you need.

Full configuration example:

The `MailgunMailService`, `DbPersistentMailService` and the `SendAndPersistMailService`, which send the email
via `mailgun` and persist 

```kotlin
@Configuration
@ComponentScan
class MailConfiguration {
    @Bean
    fun mailStorageRepository(
            dataSource: DataSource
    ): MailStorageRepository = MailStorageRepository(dataSource)

    @Bean
    fun dbPersistentMailService(
            mailTemplateContentBuilder: MailTemplateContentBuilder,
            mailStorageRepository: MailStorageRepository,
            mailServiceHelper: MailServiceHelper
    ): DbPersistentMailService = DbPersistentMailService(
                mailTemplateContentBuilder,
                mailStorageRepository,
                mailServiceHelper
        )

    @Bean
    fun mailgunMailService(
            mailTemplateContentBuilder: MailTemplateContentBuilder,
            mailServiceHelper: MailServiceHelper,
            @Value("\${mail.mailgun.base-url}")
            baseUrl: String,
            @Value("\${mail.mailgun.domain}")
            apiKey: String,
            @Value("\${mail.mailgun.api-key}")
            domain: String
    ): MailgunMailService = MailgunMailService(
            mailTemplateContentBuilder,
            mailServiceHelper,
            baseUrl,
            apiKey,
            domain
    )

    @Bean
    fun sendAndPersistMailService(
            dbPersistentMailService: DbPersistentMailService,
            mailgunMailService: MailgunMailService,
            @Value("\${mail.persistence.enabled}")
            persistMails: Boolean,
            @Value("\${mail.delivery.enabled}")
            sendMails: Boolean
    ): SendAndPersistMailService = SendAndPersistMailService(
            dbPersistentMailService,
            mailgunMailService,
            persistMails,
            sendMails
    )
```
The `StringMailService`, which returns a stringified version of an email

```kotlin
    @Bean
    fun stringMailService(
            mailTemplateContentBuilder: MailTemplateContentBuilder,
            mailServiceHelper: MailServiceHelper
    ): StringMailService = StringMailService(
            mailTemplateContentBuilder,
            mailServiceHelper
    )
```
A custom email template:

```kotlin
    @Bean
    fun myMailTemplate(
            @Value("\${mail.mymail.sender.address}")
            senderAddress: String,
            @Value("\${mail.mymail.sender.name}")
            senderName: String,
            @Value("\${mail.mymail.subject}")
            subject: String
    ): MailTemplate {
        if (senderAddress.isBlank()) {
            throw MailConfigurationException("You have to configure a sender address for your weekly insights mails")
        }

        if (subject.isBlank()) {
            throw MailConfigurationException("The subject for the weekly insights mail is not allowed to be blank")
        }

        val mailConfigBuilder = MailConfig.Builder()
                .from(senderAddress, senderName)
                .subject(subject)
                .tag("myMail")

        return MailTemplate(
                mailConfigBuilder = mailConfigBuilder,
                htmlTemplate = "myMail.html",
                txtTemplate = "myMail.txt"
        )
    }
}
```

## Templates

You need to use `thymeleaf` templates, which, by default, have to be located in `resources/mail/templates`.

## Properties

There are currently two properties you can use override the e-mail receiver globally (e.g. for testing):

```properties
mail.test.override.enabled = false
mail.test.override.receiver = invalid@timeular.com

```

The values above are the default values.

## License

The nytta email module is released under version 2.0 of the [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0
