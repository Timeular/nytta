package com.timeular.nytta.email.core

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import org.dbunit.DataSourceDatabaseTester
import org.dbunit.IDatabaseTester
import org.dbunit.IOperationListener
import org.dbunit.database.DatabaseConfig
import org.dbunit.database.IDatabaseConnection
import org.dbunit.dataset.ITable
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory
import org.dbunit.operation.DatabaseOperation
import org.h2.jdbcx.JdbcDataSource
import org.junit.jupiter.api.BeforeEach
import java.io.File
import java.math.BigInteger
import javax.sql.DataSource

abstract class DBUnitTest {
    val testDataSource: DataSource = initializeDataSource()

    private var databaseTester: IDatabaseTester = initializeDatabaseTester()

    @BeforeEach
    open fun before() {
        databaseTester.onSetup()
    }

    private fun initializeDataSource(): JdbcDataSource {
        val ds = JdbcDataSource()
        ds.setURL("jdbc:h2:~/sample")
        ds.user = "sa"
        ds.password = ""
        return ds
    }

    private fun initializeDatabaseTester(): IDatabaseTester {
        val tablesSQL = File(ClassLoader.getSystemResource("db/mail_storage.sql").file).readText()
        val con = testDataSource.connection
        val stmt = con.prepareStatement(tablesSQL)
        try {
            stmt.executeUpdate()
        } catch (e: Exception) {
            // if tables are already created, ignore
        } finally {
            stmt.close()
        }
        
        val dbTester = DataSourceDatabaseTester(testDataSource)

        val dataSet = FlatXmlDataSetBuilder().build(
                DBUnitTest::class.java.getResourceAsStream("/db/test-data.xml"))

        dbTester.dataSet = dataSet
        dbTester.setUpOperation = DatabaseOperation.CLEAN_INSERT
        dbTester.setOperationListener(OperationListener())

        return dbTester
    }

    protected fun connection(): IDatabaseConnection = databaseTester.connection

    protected fun assertColumnInFirstRow(table: ITable, columnName: String, expectedValue: String?) {
        assertThat(table.getValue(0, columnName) as String?, equalTo(expectedValue))
    }

    protected fun assertColumnInFirstRow(table: ITable, columnName: String, expectedValue: BigInteger) {
        assertThat(table.getValue(0, columnName) as BigInteger, equalTo(expectedValue))
    }

    protected fun assertColumnInFirstRow(table: ITable, columnName: String, expectedValue: Boolean) {
        assertThat(table.getValue(0, columnName) as Boolean, equalTo(expectedValue))
    }

    protected fun assertColumnInFirstRowPresent(table: ITable, columnName: String, expectedValue: Any?) {
        if (expectedValue != null) {
            assertThat(table.getValue(0, columnName), present())
        } else {
            assertThat(table.getValue(0, columnName), absent())
        }
    }
}

class OperationListener : IOperationListener {
    private val postgresqlDataTypeFactory = PostgresqlDataTypeFactory()

    override fun operationSetUpFinished(connection: IDatabaseConnection?) {

    }

    override fun operationTearDownFinished(connection: IDatabaseConnection?) {

    }

    override fun connectionRetrieved(connection: IDatabaseConnection?) {
        connection?.config?.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, postgresqlDataTypeFactory)
    }
}