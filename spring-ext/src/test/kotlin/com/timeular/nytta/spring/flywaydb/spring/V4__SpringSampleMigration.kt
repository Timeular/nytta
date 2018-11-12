package com.timeular.nytta.spring.flywaydb.spring

import com.timeular.nytta.spring.flywaydb.Migration
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.slf4j.LoggerFactory

@Migration
class V4__SpringSampleMigration() : BaseJavaMigration() {

    private companion object {
        private val logger = LoggerFactory.getLogger(V4__SpringSampleMigration::class.java)
    }

    override fun migrate(context: Context?) {
        logger.info("Migration done!")
    }
}