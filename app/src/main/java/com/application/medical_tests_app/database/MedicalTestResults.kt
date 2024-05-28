package com.application.medical_tests_app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(
    tableName = "medical_test_results",
    foreignKeys = [
        ForeignKey(
            entity = MedicalTestType::class,
            parentColumns = ["id"],
            childColumns = ["medical_test_type_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MedicalTestFile::class,
            parentColumns = ["id"],
            childColumns = ["file_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicalTestResults(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "file_id") val fileId: Int,
    @ColumnInfo(name = "medical_test_type_id") val medicalTestTypeId: Int,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "value") val value: Float,
    @ColumnInfo(name = "reference_range_min") val referenceRangeMin: Float,
    @ColumnInfo(name = "reference_range_max") val referenceRangeMax: Float
)
