package com.application.medical_tests_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LaboratoryDao {
    @Query("SELECT * FROM laboratories")
    fun getAll(): List<Laboratory>

    @Query("SELECT * FROM laboratories WHERE name =:laboratoryName")
    fun getByName(laboratoryName: String): List<Laboratory>

    @Query("SELECT * FROM laboratories WHERE id =:laboratoryId")
    fun loadByIds(laboratoryId: Int): List<Laboratory>

    @Insert
    fun insert(laboratory: Laboratory) : Long

    @Insert
    fun insertAll(vararg laboratories: Laboratory)

    @Delete
    fun delete(laboratory: Laboratory)
}