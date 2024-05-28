package com.application.medical_tests_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MedicalTestResultsDao {
    @Query("SELECT * FROM medical_test_results")
    fun getAll(): List<MedicalTestResults>

    @Query("SELECT * FROM medical_test_results WHERE id =:medicalTestResultsId")
    fun loadByIds(medicalTestResultsId: Int): List<MedicalTestResults>

    @Query("SELECT * FROM medical_test_results WHERE file_id =:fileId")
    fun loadByFile(fileId: Int): List<MedicalTestResults>

    @Insert
    fun insertAll(vararg medicalTestResults: MedicalTestResults)

    @Delete
    fun delete(medicalTestResults: MedicalTestResults)

    @Query("""
        SELECT * 
        FROM medical_test_results 
        WHERE medical_test_type_id IN (
            SELECT medical_test_type_id 
            FROM medical_test_results
            GROUP BY medical_test_type_id 
            HAVING COUNT(*) >= 2
        )
    """)
    fun getMedicalTestResultsForGraphs(): List<MedicalTestResults>
}