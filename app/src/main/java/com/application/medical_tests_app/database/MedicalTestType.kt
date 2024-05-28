package com.application.medical_tests_app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medical_tests_types")
data class MedicalTestType (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "units") var units: String = "",
)