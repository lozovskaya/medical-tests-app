package com.application.medical_tests_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.DeleteTable
import com.application.medical_tests_app.database.AppDatabase
import com.application.medical_tests_app.database.MedicalTestResults
import com.github.mikephil.charting.data.Entry
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat

class GraphsActivity : AppCompatActivity() {

    lateinit var db : AppDatabase;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphs)

        db = AppDatabase.getInstance(this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val graphDataList = loadGraphDataListFromDatabase()
        val adapter = GraphAdapter(graphDataList) 
        recyclerView.adapter = adapter

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_graphs
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
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

    private fun loadGraphDataListFromDatabase(): List<GraphAdapter.Graph> {
        return loadDataFromDatabase() + loadExampleChartData()
    }

    private fun loadDataFromDatabase() : List<GraphAdapter.Graph> {
        val validDbResults = groupResultsByTypeId(db.MedicalTestResultsDao().getMedicalTestResultsForGraphs())
        return validDbResults.map { groupedResult ->
            val referenceRangeMin = groupedResult.get(0).referenceRangeMin
            val referenceRangeMax = groupedResult.get(0).referenceRangeMax
            val resultName = db.MedicalTestDao().getByIds(groupedResult.get(0).medicalTestTypeId).get(0).name
            val sortedByDate = groupedResult.sortedBy { medicalTestResults ->
                db.MedicalTestFileDao().loadByIds(medicalTestResults.fileId).get(0).date }
            val entries : List<Entry> = sortedByDate.mapIndexed { i, medicalTestResults ->
                Entry(i.toFloat(), medicalTestResults.value)
            }
            val dateLabels = sortedByDate.map { medicalTestResults ->
                val dateFormat = SimpleDateFormat("dd.MM.yy")
                dateFormat.format(db.MedicalTestFileDao().loadByIds(medicalTestResults.fileId).get(0).date)
            }
            GraphAdapter.Graph(entries, dateLabels, resultName, referenceRangeMin, referenceRangeMax)
        }
    }

    private fun groupResultsByTypeId(results: List<MedicalTestResults>): List<List<MedicalTestResults>> {
        return results.groupBy { it.medicalTestTypeId }
            .values
            .toList()
    }

    private fun loadExampleChartData(): GraphAdapter.Graph {
        val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 20f))
        entries.add(Entry(2f, 30f))
        entries.add(Entry(3f, 25f))

        val dateLabels = listOf("31.12.22", "31.12.23", "31.12.24")
        return GraphAdapter.Graph(entries, dateLabels, "Example test", 22f, 25f)
    }
}