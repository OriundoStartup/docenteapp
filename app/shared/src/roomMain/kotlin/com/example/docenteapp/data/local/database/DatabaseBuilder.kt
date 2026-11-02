package com.example.docenteapp.data.local.database

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

expect fun configureDatabaseDriver(builder: RoomDatabase.Builder<AppDatabase>): RoomDatabase.Builder<AppDatabase>

fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return configureDatabaseDriver(builder)
        .fallbackToDestructiveMigration(true)
        .build()
}
