package com.example.docenteapp.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object UsersTable : Table("users") {
    val id = varchar("id", 64)
    val firstName = varchar("first_name", 128)
    val lastName = varchar("last_name", 128)
    val email = varchar("email", 255).uniqueIndex()
    val role = varchar("role", 32)
    val passwordHash = varchar("password_hash", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}

object CoursesTable : Table("courses") {
    val id = varchar("id", 64)
    val name = varchar("name", 255)
    val professorId = varchar("professor_id", 64).references(UsersTable.id)
    val collegeName = varchar("college_name", 255)
    val accessCode = varchar("access_code", 16).uniqueIndex()
    val period = varchar("period", 32)

    override val primaryKey = PrimaryKey(id)
}

object DatabaseFactory {
    @Volatile
    var isInitialized: Boolean = false
        private set

    fun init() {
        val jdbcUrl = System.getenv("SUPABASE_JDBC_URL")
            ?: System.getProperty("SUPABASE_JDBC_URL")

        if (jdbcUrl.isNull_or_Empty()) {
            isInitialized = false
            return
        }

        val user = System.getenv("SUPABASE_DB_USER")
            ?: System.getProperty("SUPABASE_DB_USER")
            ?: "postgres"
        val password = System.getenv("SUPABASE_DB_PASSWORD")
            ?: System.getProperty("SUPABASE_DB_PASSWORD")
            ?: "postgres"

        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            this.jdbcUrl = jdbcUrl
            username = user
            this.password = password
            maximumPoolSize = 5
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            initializationFailTimeout = 1000
            validate()
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(UsersTable, CoursesTable)
        }
        isInitialized = true
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T {
        if (!isInitialized) {
            throw IllegalStateException("Database not initialized")
        }
        return newSuspendedTransaction { block() }
    }
}

private fun String?.isNull_or_Empty(): Boolean = this == null || this.trim().isEmpty()
