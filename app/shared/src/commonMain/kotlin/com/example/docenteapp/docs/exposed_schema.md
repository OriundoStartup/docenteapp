# Estructura del Servidor Ktor y Mapeo de Base de Datos (Exposed ORM)

Este documento contiene la especificación completa del código fuente del servidor para las tablas relacionales utilizando **Exposed ORM** de JetBrains. Esta estructura cumple estrictamente con la **Tercera Forma Normal (3NF)** y servirá como base para el desarrollo del backend.

## 📁 Estructura del Proyecto Servidor Sugerida
```text
server/
├── src/
│   └── main/
│       └── kotlin/
│           └── com/example/docenteapp/
│               ├── Application.kt          # Configuración e inicio del servidor Ktor
│               ├── database/
│               │   ├── DatabaseFactory.kt  # Conexión al Pool (PostgreSQL) e inicialización
│               │   └── Tables.kt           # Definición de tablas Exposed 3NF (Este documento)
│               ├── routes/
│               │   ├── AuthRoutes.kt       # Registro y Login
│               │   ├── AcademicRoutes.kt   # Cursos, Evaluaciones, Asistencia y Notas
│               │   └── CommRoutes.kt       # Mensajería y Comunicaciones
│               └── security/
│                   └── JwtConfig.kt        # Cifrado de Tokens y Middleware
└── build.gradle.kts                        # Dependencias de Ktor y Exposed
```

---

## 💻 Código de Tablas Relacionales (`Tables.kt`)

Este código define las llaves primarias, foráneas, restricciones de unicidad e índices para garantizar un rendimiento óptimo y costo cero de mantenimiento.

```kotlin
package com.example.docenteapp.database

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import java.util.UUID

// Enumeraciones compartidas
enum class UserRole { PROFESSOR, PARENT }
enum class AttendanceStatus { PRESENT, ABSENT, LATE, JUSTIFIED }

// 1. USUARIOS Y PERFILES (Especialización por ID de referencia)
object UsersTable : UUIDTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val firstName = varchar("first_name", 100)
    val lastName = varchar("last_name", 100)
    val role = enumerationByName("role", 20, UserRole::class)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
}

object ProfessorsTable : IdTable<UUID>("professors") {
    override val id = reference("user_id", UsersTable).primaryKey()
    val specialty = varchar("specialty", 255)
    val bio = text("bio").nullable()
}

object ParentsTable : IdTable<UUID>("parents") {
    override val id = reference("user_id", UsersTable).primaryKey()
    val phone = varchar("phone", 30)
    val address = varchar("address", 255).nullable()
}

// 2. ESTRUCTURA ACADÉMICA E INSTITUCIONAL
object CollegesTable : UUIDTable("colleges") {
    val name = varchar("name", 255)
    val address = varchar("address", 255)
    val identifier = varchar("identifier", 50).uniqueIndex()
}

object CoursesTable : UUIDTable("courses") {
    val name = varchar("name", 100)
    val professorId = reference("professor_id", ProfessorsTable)
    val collegeId = reference("college_id", CollegesTable)
    val accessCode = varchar("access_code", 10).uniqueIndex()
    val period = varchar("period", 20)
}

object StudentsTable : UUIDTable("students") {
    val firstName = varchar("first_name", 100)
    val lastName = varchar("last_name", 100)
    val rut = varchar("rut", 20).uniqueIndex()
    val courseId = reference("course_id", CoursesTable)
}

// Relación Muchos a Muchos (N:M) entre Padres y Alumnos
object ParentStudentsTable : Table("parent_students") {
    val parentId = reference("parent_id", ParentsTable)
    val studentId = reference("student_id", StudentsTable)
    override val primaryKey = PrimaryKey(parentId, studentId)
}

// 3. SEPARACIÓN 3NF: EVALUACIONES, CALIFICACIONES Y ASISTENCIA
object EvaluationsTable : UUIDTable("evaluations") {
    val courseId = reference("course_id", CoursesTable)
    val description = varchar("description", 255)
    val date = date("date")
}

object GradesTable : UUIDTable("grades") {
    val studentId = reference("student_id", StudentsTable)
    val evaluationId = reference("evaluation_id", EvaluationsTable)
    val value = float("value")
}

object AttendanceTable : UUIDTable("attendance") {
    val studentId = reference("student_id", StudentsTable)
    val date = date("date")
    val status = enumerationByName("status", 20, AttendanceStatus::class)
}

// 4. CANAL DE COMUNICACIÓN (Mensajería Segura)
object CommunicationsTable : UUIDTable("communications") {
    val senderId = reference("sender_id", UsersTable)
    val receiverId = reference("receiver_id", UsersTable)
    val title = varchar("title", 255)
    val content = text("content")
    val isRead = boolean("is_read").default(false)
    val sentAt = timestamp("sent_at").defaultExpression(CurrentTimestamp())
}
```

---

## 🛠️ Configuración de Inicialización NTI (DatabaseFactory.kt)

Para activar el servidor sin escribir código SQL a mano, se usa la utilidad de sincronización automática de Exposed dentro de Ktor:

```kotlin
package com.example.docenteapp.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(databaseUrl: String) {
        // Conexión al Driver de PostgreSQL (Supabase / Neon)
        val database = Database.connect(databaseUrl, driver = "org.postgresql.Driver")
        
        transaction(database) {
            // Sincroniza y crea las tablas relacionales de forma segura si no existen
            SchemaUtils.create(
                UsersTable, ProfessorsTable, ParentsTable,
                CollegesTable, CoursesTable, StudentsTable, ParentStudentsTable,
                EvaluationsTable, GradesTable, AttendanceTable, CommunicationsTable
            )
        }
    }
}
```
