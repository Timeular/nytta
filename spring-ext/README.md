# Nytta Spring Extensions

The Nytta Spring Extensions consists of various small helping configurations to make our
life with spring easier.

## Latest release

The most recent release is spring-ext 5.0.0, released December 03, 2021.

### Maven

```xml
<dependency>
  <groupId>com.timeular.nytta</groupId>
  <artifactId>spring-ext</artifactId>
  <version>5.0.0</version>
</dependency>
```

### Gradle

```gradle
// Gradle Groovy DSL
compile 'com.timeular.nytta:spring-ext:5.0.0'

// Gradle Kotlin DSL
compile(group = "com.timeular.nytta", name = "spring-ext", version = "5.0.0")
```

### Manual

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta/spring-ext/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.timeular.nytta/spring-ext/badge.svg)
Download the latest release from the maven central repository and add the jar file to you classpath.

## Requirements

nytta spring ext is compiled against JDK8+ and has no required dependency because we don't want
any unnecessary dependencies if we are not using everything:

Optional dependencies are:

- spring
- spring-boot
- spring-retry

## Usage

### RetryableDataSource

The `RetryableDataSource` is a wrapper around a `javax.sql.DataSource`. Simply place your DataSource
and a [RetryTemplate](https://github.com/spring-projects/spring-retry) to that wrapper and use the
RetryableDataSource instead of your initial one.

```kotlin
    RetryableDataSource(dataSource, retryTemplate)
```

If you are already using a spring based application you can also include a file called `DataSourceConfiguration`
which is a java based spring configuration which wrappes all DataSources you have in your spring context
for you.

```kotlin
@Import(
    SpringBootFlywayConfiguration::class
)
```

**Note:** spring-retry is always required to use the `RetryableDataSource` and spring itself must be used to work with
the `SpringBootFlywayConfiguration`.

#### Loading Flyway Migration from the Spring Context

This feature adds the capabilities to load a migration for [Flyway DB](https://flywaydb.org/) from the spring context.

**Note:** This feature is a plain implemenation and has **switched off** all transactional mechanism of flyway. This means
that if you want to have a rollback mechanism within your migration you have to implement it by yourself!

To use this feature you have to configure an additional resolver called `SpringMigrationResolver`. You can do that bei either
configure it via the flyway api, extend your spring configuration or load `SpringBootFlywayConfiguration` in your spring boot app.

In addition to that resolver you have to create your java migration as usual but you shouldn't place it in the classpath where
flyway db is expecting it. Just place it anywhere else and apply the `Migration` annotation on top of the class.

```kotlin
@Migration
class V4__SpringSampleMigration() : BaseJavaMigration() {

    private companion object {
        private val logger = LoggerFactory.getLogger(V4__SpringSampleMigration::class.java)
    }

    override fun migrate(context: Context?) {
        logger.info("Migration done!")
    }
}
```

You can know add your migration to the spring context by adding it via xml/java configuration or by adding a component scan. If you
are sticking to the component scan then you are done for know - the `Migration` annotation is implicitly referring to `@Component`.

## License

The nytta spring extension project is released under version 2.0 of the [Apache License][].

[apache license]: http://www.apache.org/licenses/LICENSE-2.0
