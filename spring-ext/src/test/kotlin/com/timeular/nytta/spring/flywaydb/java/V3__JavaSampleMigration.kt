package com.timeular.nytta.spring.flywaydb.java

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.slf4j.LoggerFactory

class V3__JavaSampleMigration : BaseJavaMigration() {

    private companion object {
        private val logger = LoggerFactory.getLogger(V3__JavaSampleMigration::class.java)
    }

    override fun migrate(context: Context?) {
        logger.info("Java Migration done!")
    }
}