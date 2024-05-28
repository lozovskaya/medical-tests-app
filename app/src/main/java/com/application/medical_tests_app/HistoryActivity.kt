package com.application.medical_tests_app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.medical_tests_app.database.AppDatabase
import com.application.medical_tests_app.database.MedicalTestResults
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date

class HistoryActivity : AppCompatActivity() {

    lateinit var db : AppDatabase;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        db = AppDatabase.getInstance(this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val filesEntries: List<FileEntry> = db.MedicalTestFileDao().getAll().map { medicalTestFile ->
            FileEntry(findLabName(medicalTestFile.labId), dateFormat.format(medicalTestFile.date), medicalTestFile.id)
        }

        val adapter = HistoryAdapter(filesEntries) { position ->
            run {
                showMedicalTestResultsDialog(filesEntries[position].fileId)
            }
        }
        recyclerView.adapter = adapter

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_history
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
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
    }

    private fun showMedicalTestResultsDialog(fileId : Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_medical_test_results, null)

        val tableLayout: TableLayout = dialogView.findViewById(R.id.tableLayout)

        val testResults : List<MedicalTestResultForTable> =
            db.MedicalTestResultsDao().loadByFile(fileId).map { medicalTestResults ->
                val medicalTestType = db.MedicalTestDao().getByIds(medicalTestResults.medicalTestTypeId).get(0).name
                val units = db.MedicalTestDao().getByName(medicalTestType).get(0).units
                MedicalTestResultForTable(medicalTestType, medicalTestResults.value.toString(), units,
                    String.format("%.1f-%.2f", medicalTestResults.referenceRangeMin, medicalTestResults.referenceRangeMax))
            }

        for (result in testResults) {
            val tableRow = TableRow(this)

            val typeTextView = TextView(this).apply {
                text = result.medicalTestType
                setPadding(8, 8, 8, 8)
            }
            val resultTextView = TextView(this).apply {
                text = result.result
                setPadding(8, 8, 8, 8)
            }
            val unitsTextView = TextView(this).apply {
                text = result.units
                setPadding(8, 8, 8, 8)
            }
            val referenceTextView = TextView(this).apply {
                text = result.referenceValues
                setPadding(8, 8, 8, 8)
            }

            tableRow.addView(typeTextView)
            tableRow.addView(resultTextView)
            tableRow.addView(unitsTextView)
            tableRow.addView(referenceTextView)

            tableLayout.addView(tableRow)
        }

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Medical Test Results")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Delete") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun findLabName(labId : Int) : String {
        return db.LaboratoryDao().loadByIds(labId).get(0).name
    }

    data class FileEntry(
        val labName: String,
        val date: String,
        val fileId : Int
    )

    data class MedicalTestResultForTable (
        val medicalTestType: String,
        val result: String,
        val units: String,
        val referenceValues: String
    )
}