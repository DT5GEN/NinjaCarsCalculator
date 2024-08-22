package com.example.ninjacarscalculator.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface Dao {
    @Query("SELECT * FROM parametrs")
    fun getAll(): List<AllParametrs>

    @Query("SELECT * FROM parametrs WHERE id = :employeeId")
    suspend fun get(employeeId: String): AllParametrs

    @Query("SELECT id FROM parametrs")
    suspend fun getOneParamInt(): Int

    @Insert(entity = AllParametrs::class)
    suspend fun insert(name: AllParametrs)

    @Delete
    suspend fun delete(name: AllParametrs)

    @Update
    suspend fun update(name: AllParametrs)

}