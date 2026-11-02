# Guía Completa de Pruebas - DocenteApp

Este documento proporciona instrucciones paso a paso para ejecutar todas las pruebas del proyecto (Unitarias, de Interfaz e Integración) y visualizar los resultados directamente en Android Studio.

## 📁 Tipos de Pruebas en el Proyecto

1.  **Pruebas Unitarias (`shared:commonTest`):** Validan la lógica de negocio pura (UseCases) sin necesidad de emulador.
2.  **Pruebas de Interfaz (`app:androidApp:androidTest`):** Validan las pantallas de Compose simulando clics reales en un emulador o dispositivo físico.
3.  **Pruebas de Integración y Persistencia:** Validan que la lógica del Backend (Ktor) y la base de datos local **3NF** funcionen correctamente de forma aislada.
    *   `PersistenceIntegrationTest`: Prueba que las tablas se creen y relacionen bien en H2 dentro del backend.
    *   `AuthIntegrationTest`: Prueba el flujo completo de Registro y Login (API + DB).
    *   `Room Storage (:app:shared)`: La persistencia local y las relaciones 3NF están protegidas en el conjunto de fuentes exclusivas `roomMain`, asegurando la integridad de las claves foráneas en Android, iOS y Desktop.

---

## 🚀 Cómo ejecutar las pruebas desde la Terminal

Puedes ejecutar todas las pruebas usando comandos de Gradle en la terminal de Android Studio (pestaña "Terminal" en la parte inferior):

### 1. Ejecutar Pruebas Unitarias (Lógica de Negocio)
```bash
./gradlew :app:shared:test
```

### 2. Ejecutar Pruebas de Integración (Backend completo)
```bash
./gradlew :server:test
```
*Nota: Se ha configurado el entorno de pruebas para usar una base de datos H2 en memoria, por lo que NO necesitas tener PostgreSQL instalado para correr estos tests.*

### 3. Ejecutar Pruebas de UI (Pantallas de la App)
*Requiere emulador o teléfono conectado.*
```bash
./gradlew :app:androidApp:connectedDebugAndroidTest
```

---

## 📊 Cómo ver los resultados detallados en el IDE

### Método Visual (Recomendado)
1.  En el panel **Project** (Izquierda), navega hasta la carpeta de tests:
    *   Lógica: `app/shared/src/commonTest/kotlin/`
    *   Backend: `server/src/test/kotlin/`
    *   UI: `app/androidApp/src/androidTest/kotlin/`
2.  Haz clic derecho sobre la carpeta o un archivo específico.
3.  Selecciona **"Run 'Tests in...'"**.
4.  **Verás una nueva pestaña abajo llamada "Run"** con una lista de todos los tests y un check verde ✅ si pasaron o una X roja ❌ si fallaron.

### Estructura de Persistencia en KMP
> [!NOTE]
> La base de datos local basada en Room ha sido modularizada dentro del Source Set exclusivo **`roomMain`**. Esto garantiza que las dependencias oficiales de Room se validen semánticamente en entornos móviles y escritorio, aislando a la plataforma Web de referencias no resueltas.

### Solución de problemas comunes
*   **Fallo de Conexión Postgres:** Si un test de servidor falla por `PSQLException`, asegúrate de que el test esté llamando a `module(testing = true)` para usar H2 en lugar de la base de datos real.

---

> [!IMPORTANT]
> **Calidad Blindada:** Antes de realizar cualquier cambio en el servidor Ktor o la arquitectura local, asegúrate de que `./gradlew :server:test` termine con éxito. Esto garantiza que la arquitectura **3NF** se mantiene intacta en todo el ciclo de persistencia.
