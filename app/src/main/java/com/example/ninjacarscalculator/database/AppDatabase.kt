package com.example.ninjacarscalculator.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AllParametrs::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myteamDao(): Dao
}



