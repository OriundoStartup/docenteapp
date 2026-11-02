package com.example.docenteapp.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.example.docenteapp.data.local.dao.*
import com.example.docenteapp.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        ProfessorEntity::class,
        ParentEntity::class,
        CollegeEntity::class,
        CourseEntity::class,
        StudentEntity::class,
        AttendanceEntity::class,
        EvaluationEntity::class,
        GradeEntity::class,
        CommunicationEntity::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun collegeDao(): CollegeDao
    abstract fun courseDao(): CourseDao
    abstract fun studentDao(): StudentDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun gradeDao(): GradeDao
    abstract fun communicationDao(): CommunicationDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
