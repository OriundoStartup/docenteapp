package com.example.docenteapp.di

import com.example.docenteapp.data.local.*
import com.example.docenteapp.data.local.database.AppDatabase
import com.example.docenteapp.data.local.database.getDatabaseBuilder
import com.example.docenteapp.data.local.database.getRoomDatabase
import com.example.docenteapp.domain.*
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { getRoomDatabase(getDatabaseBuilder()) }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().collegeDao() }
    single { get<AppDatabase>().courseDao() }
    single { get<AppDatabase>().studentDao() }
    single { get<AppDatabase>().attendanceDao() }
    single { get<AppDatabase>().gradeDao() }
    single { get<AppDatabase>().communicationDao() }

    // Repositories
    single<UserRepository> { RoomUserRepository(get(), get(), get()) }
    single<CollegeRepository> { RoomCollegeRepository(get()) }
    single<CourseRepository> { RoomCourseRepository(get()) }
    single<CommunicationRepository> { RoomCommunicationRepository(get()) }
    single<AttendanceRepository> { RoomAttendanceRepository(get()) }
    single<GradeRepository> { RoomGradeRepository(get()) }
    single<StudentRepository> { RoomStudentRepository(get()) }
}
