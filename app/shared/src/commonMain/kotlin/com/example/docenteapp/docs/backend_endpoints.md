# Especificación de Endpoints e Integración - Ktor Server

Este documento detalla la estructura y firmas de los contratos de la API para conectar la aplicación móvil compartiendo el modelo relacional **3NF**.

## 🔐 1. Módulo de Autenticación (`AuthRoutes.kt`)

### `POST /auth/register`
Crea la cuenta de usuario centralizada y genera el perfil huérfano correspondiente en un solo paso transaccional.
* **Request Body:**
  ```json
  {
    "firstName": "Carlos",
    "lastName": "Mendoza",
    "email": "carlos@docente.cl",
    "password": "hashed_string_here",
    "role": "PROFESSOR",
    "specialty": "Matemáticas Avanzadas"
  }
  ```
* **Response (201 Created):** Objeto `User` completo estructurado en JSON con un Token JWT firmado.

### `POST /auth/login`
Valida credenciales y expone los datos de perfil relacional completos.
* **Response (200 OK):**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsIn...",
    "user": {
      "id": "uuid-v4-string",
      "firstName": "Carlos",
      "email": "carlos@docente.cl",
      "role": "PROFESSOR",
      "professorProfile": {
        "specialty": "Matemáticas Avanzadas",
        "bio": null
      }
    }
  }
  ```

---

## 📅 2. Módulo Académico y Asistencia (`AcademicRoutes.kt`)

### `POST /attendance`
Registra la asistencia en lote de manera atómica, ideal para optimizar las conexiones desde redes móviles.
* **Headers:** `Authorization: Bearer <JWT_TOKEN>`
* **Request Body:**
  ```json
  {
    "courseId": "course-uuid",
    "date": "2025-03-10",
    "records": [
      { "studentId": "student-1-uuid", "status": "PRESENT" },
      { "studentId": "student-2-uuid", "status": "ABSENT" }
    ]
  }
  ```

### `POST /grades` (Punto crítico 3NF)
Registra las notas vinculándolas a una evaluación global del curso en lugar de duplicar strings por cada estudiante.
* **Request Body:**
  ```json
  {
    "courseId": "course-uuid",
    "description": "Solemne 1 de Álgebra",
    "date": "2025-03-12",
    "scores": [
      { "studentId": "student-1-uuid", "value": 6.5 },
      { "studentId": "student-2-uuid", "value": 4.0 }
    ]
  }
  ```

---

## 📧 3. Módulo de Canales de Mensajería (`CommRoutes.kt`)

### `POST /communications`
Envía mensajes directos controlando la trazabilidad de lectura.
* **Request Body:**
  ```json
  {
    "receiverId": "parent-user-uuid",
    "title": "Citación a entrevista",
    "content": "Estimado apoderado, se solicita su presencia el día viernes..."
  }
  ```

### `GET /communications/inbox`
Lista los mensajes del usuario ordenados cronológicamente de forma descendente.
* **Response (200 OK):** Lista de mensajes relacionales incluyendo metadatos `isRead` y `sentAt`.
