package com.application.medical_tests_app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date


@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val fullName: String?,
    @ColumnInfo(name = "gender") val gender: Int,
    @ColumnInfo(name = "birthday") @TypeConverters(Converters::class) val birthday: Date,
)
