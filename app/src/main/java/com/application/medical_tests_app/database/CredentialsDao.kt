package com.application.medical_tests_app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CredentialsDao {
    @Query("SELECT * FROM credentials")
    fun getAll(): List<Credentials>

    @Query("SELECT * FROM credentials WHERE user_id =:userId")
    fun loadByUserId(userId: Int): List<Credentials>

    @Insert
    fun insertAll(vararg credentials: Credentials)

    @Delete
    fun delete(credentials: Credentials)
}