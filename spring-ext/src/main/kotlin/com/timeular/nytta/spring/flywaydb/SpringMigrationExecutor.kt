package com.timeular.nytta.spring.flywaydb

import org.flywaydb.core.api.FlywayException
import org.flywaydb.core.api.configuration.Configuration
import org.flywaydb.core.api.executor.Context
import org.flywaydb.core.api.executor.MigrationExecutor
import org.flywaydb.core.api.migration.JavaMigration
import java.sql.Connection

class SpringMigrationExecutor(
        private val javaMigration: JavaMigration
) : MigrationExecutor {

    override fun canExecuteInTransaction(): Boolean = false

    override fun execute(context: Context?) {
        context?.run {
            try {
                javaMigration.migrate(ApiContextWrapper(this))
            } catch (ex: Exception) {
                throw FlywayException("Migration failed!", ex)
            }
        } ?: throw FlywayException("Migration failed: Provided context is null!")
    }

    private class ApiContextWrapper(
            val executionContext: Context
    ) : org.flywaydb.core.api.migration.Context {
        override fun getConfiguration(): Configuration = executionContext.configuration

        override fun getConnection(): Connection = executionContext.connection
    }
}