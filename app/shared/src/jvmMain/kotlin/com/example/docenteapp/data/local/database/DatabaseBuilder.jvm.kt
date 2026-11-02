package com.example.docenteapp.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "docente_app.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}

actual fun configureDatabaseDriver(builder: RoomDatabase.Builder<AppDatabase>): RoomDatabase.Builder<AppDatabase> {
    return builder.setDriver(BundledSQLiteDriver())
}
