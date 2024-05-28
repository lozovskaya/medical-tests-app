package com.application.medical_tests_app

import com.application.medical_tests_app.database.AppDatabase
import com.application.medical_tests_app.database.Laboratory
import com.application.medical_tests_app.database.MedicalTestFile
import com.application.medical_tests_app.database.MedicalTestResults
import com.application.medical_tests_app.database.MedicalTestType
import java.util.Date

fun getMedicalTestTypeId(db : AppDatabase, name : String, units : String) : Int {
    var medicalTestTypeId : Int
    if (db.MedicalTestDao().getByName(name).isEmpty()) {
        val newMedicalTestType = MedicalTestType(name = name, units = units)
        medicalTestTypeId = db.MedicalTestDao().insert(newMedicalTestType).toInt()
    } else {
        medicalTestTypeId = db.MedicalTestDao().getByName(name).get(0).id
    }
    return medicalTestTypeId
}

fun addManualMedicalTestResultsToDB(db : AppDatabase, manualResult: TestResult) {
    val medicalTestTypeId = getMedicalTestTypeId(db, manualResult.medicalTestType, manualResult.units)
    if (db.LaboratoryDao().getByName("Manual entering").isEmpty()) {
        db.LaboratoryDao().insertAll(Laboratory(100500, "Manual entering", "none"))
    }
    val newFile = MedicalTestFile(userId = 1, labId = 100500, date = manualResult.date, filePath = "")
    val fileID = db.MedicalTestFileDao().insert(newFile).toInt()

    db.MedicalTestResultsDao().insertAll(MedicalTestResults(
        fileId = fileID,
        medicalTestTypeId = medicalTestTypeId,
        referenceRangeMin = manualResult.referenceRangeMin,
        referenceRangeMax = manualResult.referenceRangeMax,
        value = manualResult.value))
}

fun addFileWithResultsToDB(db : AppDatabase, file: NewMedicalTestFile, results : List<TestResult>) {
    var labId : Int
    if (db.LaboratoryDao().getByName(file.labName).isEmpty()) {
        labId = db.LaboratoryDao().insert(Laboratory(name = file.labName)).toInt()
    } else {
        labId = db.LaboratoryDao().getByName(file.labName).get(0).id
    }
    val newFile = MedicalTestFile(userId = 1, labId = labId, date = file.date, filePath = file.path)
    val fileID = db.MedicalTestFileDao().insert(newFile).toInt()

    results.forEach { result ->
        db.MedicalTestResultsDao().insertAll(MedicalTestResults(
            fileId = fileID,
            medicalTestTypeId = getMedicalTestTypeId(db, result.medicalTestType, result.units),
            referenceRangeMin = result.referenceRangeMin,
            referenceRangeMax = result.referenceRangeMax,
            value = result.value))
    }
}

data class NewMedicalTestFile(
    val labName: String,
    val date: Date,
    val path: String,
)

data class TestResult (
    val medicalTestType: String,
    val value: Float,
    val date: Date,
    val referenceRangeMin: Float,
    val referenceRangeMax: Float,
    val units : String,
    val comments : String = "",
)
