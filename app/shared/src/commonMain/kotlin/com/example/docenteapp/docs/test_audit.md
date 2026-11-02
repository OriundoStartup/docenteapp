# Auditoría Técnica de Testing

Resumen de la estrategia de pruebas implementada en el proyecto.

## ✅ Pruebas de Dominio (Unit Tests)
- **Ubicación:** `app/shared/src/commonTest/kotlin/com/example/docenteapp/domain/usecases/UseCaseTests.kt`
- **Alcance:** Validación de lógica de negocio pura.
- **Pruebas Actuales:** 5 tests validados (Login, Registro, Creación de Cursos).

## 📱 Pruebas de Interfaz (Instrumented UI Tests)
- **Ubicación:** `app/androidApp/src/androidTest/kotlin/com/example/docenteapp/`
- **Tecnología:** Compose UI Test.
- **Suites:**
  - `LoginUiTest`: Flujo de entrada y validación de botones.
  - `RegisterUiTest`: Selección de roles y creación de perfiles.
  - `ProfessorDashboardUiTest`: Renderizado de tarjetas de curso.
  - `AttendanceScreenUiTest`: Interacción con la lista de alumnos.

## 🛡️ Validaciones Críticas (Seguridad y Datos)
1. **Emails:** Formato verificado en Login y Registro.
2. **Cursos:** Bloqueo de creación sin campos obligatorios.
3. **Mensajería:** Impedimento de envío de contenido vacío.
4. **Códigos de Acceso:** Longitud fija de 6 caracteres verificada en UI.
