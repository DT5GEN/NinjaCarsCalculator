package com.example.ninjacarscalculator.database

import android.app.Application
import androidx.room.Room

class App : Application() {

    lateinit var db: AppDatabase
    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db"
        ).build()
    }
}