package com.timeular.nytta.spring.flywaydb

import org.flywaydb.core.api.FlywayException
import org.flywaydb.core.api.MigrationType
import org.flywaydb.core.api.migration.JavaMigration
import org.flywaydb.core.api.resolver.Context
import org.flywaydb.core.api.resolver.ResolvedMigration
import org.flywaydb.core.internal.resolver.AbstractJavaMigrationResolver
import org.flywaydb.core.internal.resolver.ResolvedMigrationComparator
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.*


@Component
class SpringMigrationResolver(
        val appCtx: ApplicationContext
) : AbstractJavaMigrationResolver<JavaMigration, SpringMigrationExecutor>(null, null) {
    override fun resolveMigrations(context: Context): List<ResolvedMigration> {
        val migrations = resolveMigrationsByAnnotations()

        Collections.sort(migrations, ResolvedMigrationComparator())
        return migrations.toMutableList()
    }

    private fun resolveMigrationsByAnnotations(): List<ResolvedMigration> {
        val beanMap = appCtx.getBeansWithAnnotation(Migration::class.java)

        return beanMap.mapNotNull { entry ->
            if (entry.value is JavaMigration) {
                val migration = entry.value as JavaMigration

                val migrationInfo = extractMigrationInfo(migration)
                migrationInfo.executor = createExecutor(migration)
                migrationInfo
            } else {
                null
            }
        }
    }

    override fun getMigrationTypeStr(): String = "Spring"

    override fun createExecutor(migration: JavaMigration?): SpringMigrationExecutor =
            migration?.let {
                SpringMigrationExecutor(migration)
            } ?: throw FlywayException("Migration failed: No migration found!")

    override fun getMigrationType(): MigrationType = MigrationType.CUSTOM

    override fun getImplementedInterface(): Class<JavaMigration> = JavaMigration::class.java
}