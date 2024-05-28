package com.application.medical_tests_app

import PdfParserApiClient.PdfParseCallback
import PdfParserApiClient.parsePdfTest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.application.medical_tests_app.database.AppDatabase
import com.application.medical_tests_app.database.Credentials
import com.application.medical_tests_app.database.Laboratory
import com.application.medical_tests_app.database.MedicalTestFile
import com.application.medical_tests_app.database.MedicalTestType
import com.application.medical_tests_app.database.MedicalTestResults
import com.application.medical_tests_app.database.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class MainActivity : AppCompatActivity() {

    private val REQUEST_PICK_PDF = 1002
    lateinit var db : AppDatabase;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_graphs -> {
                    val intent = Intent(this, GraphsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val manEnterView = findViewById<CardView>(R.id.manualAddView)
        manEnterView.setOnClickListener {
            handleManualEntering()
        }

        val uploadCardView = findViewById<CardView>(R.id.uploadCardView)
        uploadCardView.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                "Upload Card Clicked",
                Toast.LENGTH_SHORT
            ).show()
            openFilePicker()
        }

        db = AppDatabase.getInstance(this)

        if (db.userDao().getAll().isEmpty()) {
            fill_db()
        }
    }

    private fun handleManualEntering() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_man_enter, null)

        val etAnalysisName = dialogView.findViewById<EditText>(R.id.et_test_name)
        val etResult = dialogView.findViewById<EditText>(R.id.et_result)
        val etDate = dialogView.findViewById<EditText>(R.id.et_date)
        etDate.setOnClickListener { showDatePicker(etDate) }
        val etReferenceValueMin = dialogView.findViewById<EditText>(R.id.et_reference_value_min)
        val etReferenceValueMax = dialogView.findViewById<EditText>(R.id.et_reference_value_max)
        val etUnits = dialogView.findViewById<EditText>(R.id.et_units)
        val etComments = dialogView.findViewById<EditText>(R.id.et_comments)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Please, enter your medical test results:")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val analysisName = etAnalysisName.text.toString()
                val result = if (etResult.text.toString() == "") 0f else etResult.text.toString().toFloat()
                val dateStr = etDate.text.toString()
                val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                val date = dateFormat.parse(dateStr)!!
                val referenceValueMin = if (etReferenceValueMin.text.toString() == "") 0f else etReferenceValueMin.text.toString().toFloat()
                val referenceValueMax = if (etReferenceValueMax.text.toString() == "") 0f else etReferenceValueMax.text.toString().toFloat()
                val units = etUnits.text.toString()
                val comments = etComments.text.toString()

                addManualMedicalTestResultsToDB(db, TestResult(analysisName, result,
                    date, referenceValueMin, referenceValueMax, units, comments)
                )
                Toast.makeText(this, "Test results were successfully saved", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    fun showDatePicker(birthdayInput: EditText) {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                calendar.apply {
                    set(year, month, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                val selectedDate = dateFormat.format(calendar.time)
                birthdayInput.setText(selectedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun fill_db() {
        val users = listOf(
            User(1, "John", 1, Date(1990, 5, 15)),
            User(2, "Alice", 2, Date(1985, 10, 20)),
            User(3, "Bob", 1, Date(1988, 3, 28)),
            User(4, "Emma", 2, Date(1995, 7, 10))
        )

        db.userDao().insertAll(*users.toTypedArray())

        val credentials = listOf(
            Credentials(10, 1, "john@example.com", "hashed_pass1"),
            Credentials(11, 2,"alice@example.com", "hashed_pass2"),
            Credentials(12,3, "bob@example.com", "hashed_pass3"),
            Credentials(13, 4, "emma@example.com", "hashed_pass4")
        )

        db.CredentialsDao().insertAll(*credentials.toTypedArray())

        val laboratories = listOf(
            Laboratory(11, "LabCorp", "labcorp@example.com"),
            Laboratory(22, "Quest Diagnostics", "quest@example.com"),
            Laboratory(33, "Mayo Clinic", "mayo@example.com"),
            Laboratory(44, "Clinical Pathology", "cp@example.com")
        )

        db.LaboratoryDao().insertAll(*laboratories.toTypedArray())

        val medicalTestTypes = listOf(
            MedicalTestType(1, "Blood Glucose", "Test to measure blood sugar levels", "mg/dL"),
            MedicalTestType(2, "Cholesterol", "Test to measure cholesterol levels", "mg/dL"),
            MedicalTestType(3, "Hemoglobin A1c", "Test to measure average blood sugar", "%"),
            MedicalTestType(4, "Thyroid Panel", "Test to evaluate thyroid function", "uIU/mL")
        )
        db.MedicalTestDao().insertAll(*medicalTestTypes.toTypedArray())

        val medicalTestFiles = listOf(
            MedicalTestFile(1000, 1, 11, Date(2024, 1, 1), ""),
        )

        db.MedicalTestFileDao().insertAll(*medicalTestFiles.toTypedArray())


        val medicalTestResults = listOf(
            MedicalTestResults(111, 1000, 11, 120f,70f, 140f),
            MedicalTestResults(222, 1000, 22,  200f, 125f, 200f),
            MedicalTestResults(333, 1000, 33,  6.5f, 4.0f, 5.6f),
            MedicalTestResults(444, 1000, 44, 2.5f, 0.5f, 4.5f)
        )

        db.MedicalTestResultsDao().insertAll(*medicalTestResults.toTypedArray())
    }


    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    .addCategory(Intent.CATEGORY_OPENABLE)
                    .setType("application/pdf")
        startActivityForResult(intent, REQUEST_PICK_PDF)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PICK_PDF && resultCode == RESULT_OK && data != null) {
            val pdfUri = data.data
            if (pdfUri != null && pdfUri.path != null) {
                parsePDF(this, pdfUri.path!!)
            } else {
                Toast.makeText(this, "Error: the PDF was uploaded incorrectly", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun parsePDF(context: Context, pdfPath: String) {
        try {
            parsePdfTest(object : PdfParseCallback {
                override fun onSuccess(parsedData: List<PdfParserApiClient.MedicalTestResultFromPDF>?) {
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
                    val date = dateFormat.parse("15.01.2024")!!
                    val results : List<TestResult> = parsedData!!.map { pdfResult ->
                        TestResult(medicalTestType = pdfResult.name,
                            value = pdfResult.result,
                            date = date,
                            referenceRangeMin = pdfResult.referenceRangeMin,
                            referenceRangeMax = pdfResult.referenceRangeMax,
                            units = pdfResult.units)};

                    addFileWithResultsToDB(db, NewMedicalTestFile("Invitro", date, pdfPath), results)
                    Toast.makeText(context, "Parsing successful", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(errorMessage: String?) {
                    this@MainActivity.runOnUiThread {
                        if (errorMessage != null) {
                            Log.e("MainActivity", errorMessage)
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при чтении PDF-файла", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}