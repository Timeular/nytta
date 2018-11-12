package com.timeular.nytta.spring.boot.flywaydb

import com.timeular.nytta.spring.flywaydb.SpringMigrationResolver
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@ComponentScan(value = ["com.timeular.nytta.spring.flywaydb", "com.timeular.nytta.spring.boot.flywaydb"])
open class SpringBootFlywayConfiguration

@Component
class SpringBootFlywayConfigurationCustomizer(
        private val springMigrationResolver: SpringMigrationResolver
) : FlywayConfigurationCustomizer {

    override fun customize(configuration: FluentConfiguration?) {
        configuration?.resolvers(springMigrationResolver)
    }
}