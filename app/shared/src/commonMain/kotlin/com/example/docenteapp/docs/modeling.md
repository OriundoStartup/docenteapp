# Modelado de Datos: DocenteApp

Este documento describe las entidades principales del sistema y sus relaciones.

## 📊 Entidades Principales

### Usuarios y Roles
- **User:** Entidad base para Profesores y Apoderados.
- **UserRole:** Enum con valores `PROFESSOR` y `PARENT`.

### Gestión Académica
- **College:** Representa el establecimiento educacional.
- **Course:** Creado por un Profesor. Genera un `accessCode` de 6 caracteres para vinculación.
- **Student:** Creado por un Apoderado al ingresar el código de un curso. Vincula al alumno con el curso y el padre.

### Control y Registro
- **Attendance:** Registro de presencia/ausencia de un alumno en una fecha específica.
- **Evaluation:** Define una instancia de evaluación (descripción y fecha).
- **Grade:** Nota numérica obtenida por un alumno en una evaluación específica.

### Comunicación
- **Communication:** Mensaje bidireccional entre sender y receiver (basado en emails o IDs).

## 🔗 Relaciones Clave
1. **PROFESSOR** posee múltiples **COURSES**.
2. **COURSE** pertenece a un **COLLEGE**.
3. **PARENT** vincula múltiples **STUDENTS** mediante códigos de curso.
4. **STUDENT** pertenece a un **COURSE**.
5. **EVALUATION** pertenece a un **COURSE**.
6. **GRADE** vincula un **STUDENT** con una **EVALUATION**.
