package com.timeular.nytta.spring.flywaydb

import org.springframework.stereotype.Component

/**
 * Defines a migration which should be loaded by Flyway from the spring context. Make sure
 * that the class which contains this annotation implements the {@link org.flywaydb.core.api.migration.JavaMigration}.
 * You can place classes with this annotation everywhere except at the same place where Flyway JavaMigrations are
 * placed.
 *
 * Note: If you are using such Migration annotation please be aware that you must handle rollback mechanism by yourself.
 *
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class Migration(
        val value: String = ""
)