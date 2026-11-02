package com.example.docenteapp.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

// This needs to be initialized from Android application
private lateinit var applicationContext: Context

fun initDatabaseBuilder(context: Context) {
    applicationContext = context.applicationContext
}

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = applicationContext.getDatabasePath("docente_app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = applicationContext,
        name = dbFile.absolutePath
    )
}

actual fun configureDatabaseDriver(builder: RoomDatabase.Builder<AppDatabase>): RoomDatabase.Builder<AppDatabase> {
    // Retornamos el builder sin setDriver(BundledSQLiteDriver()) para que en Android
    // se utilice el driver nativo del framework (AndroidSQLiteDriver / FrameworkSQLiteOpenHelper).
    // Esto permite la integración completa con Android Studio App Inspector / Database Inspector.
    return builder
}
