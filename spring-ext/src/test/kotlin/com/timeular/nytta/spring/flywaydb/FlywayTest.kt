package com.timeular.nytta.spring.flywaydb

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.dbunit.DataSourceDatabaseTester
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.sql.DataSource

@Configuration
@SpringBootApplication
open class FlywayTestApplication

@SpringBootTest(classes = [
    FlywayTestApplication::class
])
@ExtendWith(SpringExtension::class)
class FlywayTest(
        @Autowired
        private val dataSource: DataSource
) {

    @Test
    fun testFlywayMigration() {
        val dbTester = DataSourceDatabaseTester(dataSource)

        assertThat(dbTester.connection.getRowCount("\"schema_version\""), equalTo(5))
    }
}