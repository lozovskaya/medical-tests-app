package com.application.medical_tests_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MedicalTestFileDao {
    @Query("SELECT * FROM medical_test_files")
    fun getAll(): List<MedicalTestFile>

    @Query("SELECT * FROM medical_test_files WHERE id =:medicalTestsFilesId")
    fun loadByIds(medicalTestsFilesId: Int): List<MedicalTestFile>

    @Insert
    fun insert(medicalTestsFile: MedicalTestFile) : Long

    @Insert
    fun insertAll(vararg medicalTestsFiles: MedicalTestFile)

    @Delete
    fun delete(medicalTestsFiles: MedicalTestFile)
}