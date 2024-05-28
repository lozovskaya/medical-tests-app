package com.application.medical_tests_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MedicalTestTypeDao {
    @Query("SELECT * FROM medical_tests_types")
    fun getAll(): List<MedicalTestType>

    @Query("SELECT * FROM medical_tests_types WHERE name =:medicalTestName")
    fun getByName(medicalTestName: String): List<MedicalTestType>

    @Query("SELECT * FROM medical_tests_types WHERE id =:medicalTestId")
    fun getByIds(medicalTestId: Int): List<MedicalTestType>

    @Insert
    fun insert(medicalTestType: MedicalTestType) : Long

    @Insert
    fun insertAll(vararg medicalTestTypes: MedicalTestType)

    @Delete
    fun delete(medicalTestType: MedicalTestType)
}