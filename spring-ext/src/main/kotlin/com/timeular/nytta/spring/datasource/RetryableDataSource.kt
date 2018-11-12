package com.timeular.nytta.spring.datasource

import org.springframework.jdbc.datasource.AbstractDataSource
import org.springframework.retry.support.RetryTemplate
import java.sql.Connection
import javax.sql.DataSource

class RetryableDataSource(
        private val dataSourceDelegate: DataSource,
        private val retryTemplate: RetryTemplate
) : AbstractDataSource() {

    override fun getConnection(): Connection =
            retryTemplate.execute<Connection, Throwable> {
                dataSourceDelegate.connection
            }

    override fun getConnection(username: String?, password: String?): Connection =
            retryTemplate.execute<Connection, Throwable> {
                dataSourceDelegate.getConnection(username, password)
            }
}