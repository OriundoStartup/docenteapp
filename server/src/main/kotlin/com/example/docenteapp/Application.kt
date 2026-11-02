@file:OptIn(ExperimentalTime::class)

package com.example.docenteapp

import com.example.docenteapp.database.CoursesTable
import com.example.docenteapp.database.DatabaseFactory
import com.example.docenteapp.database.UsersTable
import com.example.docenteapp.domain.models.Course
import com.example.docenteapp.domain.models.User
import com.example.docenteapp.domain.models.UserRole
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }

    // Inicializar conexión a Supabase PostgreSQL si está configurada
    try {
        DatabaseFactory.init()
        log.info("Base de datos Supabase conectada exitosamente.")
    } catch (e: Exception) {
        log.warn("No se pudo conectar a Supabase PostgreSQL (se usarán datos en memoria/mock): ${e.message}")
    }

    routing {
        get("/") {
            call.respond(mapOf("status" to "Backend Running", "project" to "DocenteApp", "database" to "Supabase PostgreSQL Ready"))
        }

        post("/login") {
            val credentials = runCatching { call.receive<Map<String, String>>() }.getOrNull()
            val email = credentials?.get("email") ?: "test@example.com"

            var userFound: User? = null
            try {
                userFound = DatabaseFactory.dbQuery {
                    UsersTable.selectAll().where { UsersTable.email eq email }
                        .map {
                            User(
                                id = it[UsersTable.id],
                                firstName = it[UsersTable.firstName],
                                lastName = it[UsersTable.lastName],
                                email = it[UsersTable.email],
                                role = runCatching { UserRole.valueOf(it[UsersTable.role]) }.getOrDefault(UserRole.PROFESSOR),
                                createdAt = Clock.System.now()
                            )
                        }.singleOrNull()
                }
            } catch (_: Exception) {}

            val responseUser = userFound ?: User(
                id = "1",
                firstName = "Professor",
                lastName = "Tester",
                email = email,
                role = UserRole.PROFESSOR,
                createdAt = Clock.System.now()
            )

            call.respond(responseUser)
        }

        get("/courses") {
            val professorId = call.parameters["professorId"]
            var coursesList: List<Course> = emptyList()

            try {
                coursesList = DatabaseFactory.dbQuery {
                    val query = if (professorId != null) {
                        CoursesTable.selectAll().where { CoursesTable.professorId eq professorId }
                    } else {
                        CoursesTable.selectAll()
                    }
                    query.map {
                        Course(
                            id = it[CoursesTable.id],
                            name = it[CoursesTable.name],
                            professorId = it[CoursesTable.professorId],
                            collegeId = it[CoursesTable.collegeName],
                            accessCode = it[CoursesTable.accessCode],
                            period = it[CoursesTable.period]
                        )
                    }
                }
            } catch (_: Exception) {}

            if (coursesList.isEmpty()) {
                coursesList = listOf(
                    Course("1", "Matemáticas 101", professorId ?: "unknown", "Colegio Central", "MAT101", "2026-1"),
                    Course("2", "Física Avanzada", professorId ?: "unknown", "Colegio Central", "FIS202", "2026-1")
                )
            }

            call.respond(coursesList)
        }
    }
}
