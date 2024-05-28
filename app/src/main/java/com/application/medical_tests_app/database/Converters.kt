package com.application.medical_tests_app.database

import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.format(date)
    }

    @TypeConverter
    fun toDate(dateStr: String): Date {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.parse(dateStr)!!
    }
}