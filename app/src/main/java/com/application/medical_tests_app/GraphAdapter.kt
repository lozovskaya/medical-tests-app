package com.application.medical_tests_app

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class GraphAdapter(private val graphDataList: List<Graph>) :
    RecyclerView.Adapter<GraphAdapter.GraphViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_graph, parent, false)
        return GraphViewHolder(view)
    }

    override fun onBindViewHolder(holder: GraphViewHolder, position: Int) {
        val graphData = graphDataList[position]
        holder.bindData(graphData)
    }

    override fun getItemCount(): Int {
        return graphDataList.size
    }

    inner class GraphViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lineChartItemTitle: TextView = itemView.findViewById(R.id.lineChartItemTitle)
        private val lineChartItem: LineChart = itemView.findViewById(R.id.lineChartItem)

        fun bindData(graphData: Graph) {
            lineChartItemTitle.text = graphData.label
            setupChart(lineChartItem, graphData)
            val dataSet = LineDataSet(graphData.entries, graphData.label)
            dataSet.color = R.color.orange
            dataSet.valueTextColor = Color.BLACK
            val lineData = LineData(dataSet)
            lineChartItem.data = lineData

            lineChartItem.invalidate()
        }

        private fun setupChart(lineChart: LineChart, graphData: Graph) {
            lineChart.apply {
                setTouchEnabled(true)
                setPinchZoom(true)
                description.isEnabled = false
                legend.isEnabled = false

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(true)
                xAxis.valueFormatter = IndexAxisValueFormatter(graphData.dateLabels)
                xAxis.labelRotationAngle = 45f

                axisRight.isEnabled = false
                axisLeft.setDrawGridLines(true)

                val referenceMinLine = LimitLine(graphData.referenceRangeMin, "Reference range min")
                referenceMinLine.enableDashedLine(10f, 10f, 0f)

                val referenceMaxLine = LimitLine(graphData.referenceRangeMax, "Reference range max")
                referenceMaxLine.enableDashedLine(10f, 10f, 0f)

                axisLeft.addLimitLine(referenceMinLine)
                axisLeft.addLimitLine(referenceMaxLine)
            }
        }
    }

    data class Graph(
        val entries : List<Entry>,
        val dateLabels : List<String>,
        val label : String,
        val referenceRangeMin : Float,
        val referenceRangeMax : Float,
    )
}
