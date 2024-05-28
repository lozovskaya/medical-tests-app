package com.application.medical_tests_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [User::class, Credentials::class, Laboratory::class, MedicalTestType::class, MedicalTestFile::class, MedicalTestResults::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun CredentialsDao(): CredentialsDao
    abstract fun LaboratoryDao(): LaboratoryDao
    abstract fun MedicalTestDao(): MedicalTestTypeDao
    abstract fun MedicalTestResultsDao(): MedicalTestResultsDao
    abstract fun MedicalTestFileDao() : MedicalTestFileDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context,AppDatabase::class.java,"app-database.db")
                    .addMigrations(MIGRATION_1_TO_3)
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as AppDatabase
        }

        private val MIGRATION_1_TO_3 = object : Migration(1, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE medical_tests")
                db.execSQL("DROP TABLE medical_test_results")
                db.execSQL("DROP TABLE medical_test_files")

                db.execSQL("CREATE TABLE IF NOT EXISTS medical_tests_types (\n" +
                        "    name TEXT NOT NULL,\n" +
                        "    description TEXT NOT NULL,\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "    units TEXT NOT NULL\n" +
                        ")")
                db.execSQL("CREATE TABLE IF NOT EXISTS medical_test_results (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "    file_id INTEGER NOT NULL,\n" +
                        "    medical_test_type_id INTEGER NOT NULL,\n" +
                        "    value REAL NOT NULL,\n" +
                        "    reference_range_min REAL NOT NULL,\n" +
                        "    reference_range_max REAL NOT NULL,\n" +
                        "    FOREIGN KEY (medical_test_type_id) REFERENCES medical_tests_types(id) ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (file_id) REFERENCES medical_test_files(id) ON DELETE CASCADE\n" +
                        ");\n")
                db.execSQL("CREATE TABLE IF NOT EXISTS medical_test_files (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "    user_id INTEGER NOT NULL,\n" +
                        "    lab_id INTEGER NOT NULL,\n" +
                        "    date TEXT NOT NULL,\n" +
                        "    file_path TEXT NOT NULL,\n" +
                        "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,\n" +
                        "    FOREIGN KEY (lab_id) REFERENCES laboratories(id) ON DELETE CASCADE\n" +
                        ")")
            }
        }
    }
}