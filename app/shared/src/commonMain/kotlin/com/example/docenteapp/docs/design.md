# Arquitectura de DocenteApp

Este documento describe la arquitectura actual de **DocenteApp**, basada en **Kotlin Multiplatform (KMP)** y principios de **Arquitectura Limpia**.

## 🏗️ Estructura de Capas

El proyecto se divide en tres capas principales dentro del módulo `:shared`:

### 1. Dominio (Domain)
- **Modelos:** Definidos en `com.example.docenteapp.domain.models.Models`. Contiene las entidades puras (User, Course, Student, Communication, etc.).
- **Repositorios (Interfaces):** Definidos en `com.example.docenteapp.domain.Repositories`. Contratos que abstraen la persistencia.
- **Casos de Uso (UseCases):** Definidos en `com.example.docenteapp.domain.usecases.UseCases`. Contienen la lógica de negocio validada.

### 2. Datos (Data)
- **Implementaciones:** Localizadas en `com.example.docenteapp.data.SettingsRepositories`.
- **Tecnología:** Utiliza `Multiplatform Settings` para almacenamiento local persistente con serialización JSON.

### 3. Presentación (UI)
- **Arquitectura:** MVVM (Model-View-ViewModel).
- **ViewModels:** Localizados en `com.example.docenteapp.ui.viewmodels`. Cada flujo (Auth, Course, Communication) tiene su propio ViewModel especializado.
- **UI:** 100% Jetpack Compose / Compose Multiplatform.
- **Navegación:** Implementada en `com.example.docenteapp.ui.navigation.Navigation` usando `NavHost` tipado.

## 🛠️ Tecnologías Clave
- **DI:** Koin (`com.example.docenteapp.di.Di`) para inyección de dependencias.
- **Serialización:** kotlinx-serialization.
- **Fechas:** kotlinx-datetime 0.8.0 y kotlin.time.
- **UI:** Compose Multiplatform con Material 3 estable.
