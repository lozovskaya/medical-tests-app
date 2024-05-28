package com.application.medical_tests_app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(
    tableName = "medical_test_files",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Laboratory::class,
            parentColumns = ["id"],
            childColumns = ["lab_id"],
            onDelete = ForeignKey.CASCADE
        ) ,
    ]
)
data class MedicalTestFile (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "lab_id") val labId: Int,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "file_path") val filePath: String,
)