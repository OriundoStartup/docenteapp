package com.example.docenteapp.di

import com.example.docenteapp.data.*
import com.example.docenteapp.data.remote.AuthApiService
import com.example.docenteapp.data.remote.HttpClientFactory
import com.example.docenteapp.domain.*
import com.example.docenteapp.domain.usecases.*
import com.example.docenteapp.ui.viewmodels.*
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.Module

expect val platformModule: Module

val appModule = module {
    single { Settings() }

    // Network
    single { HttpClientFactory.create() }
    single { AuthApiService(get()) }

    // Settings-based or static repositories that are cross-platform
    single<EvaluationRepository> { SettingsEvaluationRepository(get()) }

    // UseCases
    factory { GetSessionUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { CreateCourseUseCase(get(), get(), get()) }
    factory { UpdateCourseUseCase(get()) }
    factory { DeleteCourseUseCase(get()) }
    factory { GetCourseDetailUseCase(get()) }
    factory { GetStudentDetailUseCase(get()) }
    factory { GetCourseProfessorsUseCase(get(), get()) }
    factory { GetStudentsByCourseUseCase(get()) }
    factory { GetParentCoursesUseCase(get(), get(), get()) }
    factory { GetCommunicationsUseCase(get(), get()) }
    factory { LinkStudentUseCase(get(), get(), get()) }
    factory { GetProfessorCoursesUseCase(get(), get()) }
    factory { GetParentStudentsUseCase(get(), get()) }
    factory { SendCommunicationUseCase(get(), get()) }
    factory { SaveAttendanceUseCase(get()) }
    factory { GenerateCertificateUseCase() }

    // ViewModels
    viewModelOf(::UserSessionViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::CourseViewModel)
    viewModelOf(::ParentViewModel)
    viewModelOf(::LinkStudentViewModel)
    viewModelOf(::CommunicationViewModel)
    viewModelOf(::AttendanceViewModel)
    viewModelOf(::GradesViewModel)
    viewModelOf(::AttendanceViewModel)
    viewModelOf(::CommunicationViewModel)
    viewModelOf(::StudentDetailViewModel)
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule, platformModule)
    }
