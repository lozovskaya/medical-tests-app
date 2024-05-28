package com.application.medical_tests_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val labEntries: List<HistoryActivity.FileEntry>,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.LabViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lab_entry, parent, false)
        return LabViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabViewHolder, position: Int) {
        if (position == labEntries.size - 1) {
            var params : RecyclerView.LayoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
            params.bottomMargin = 200;
            holder.itemView.setLayoutParams(params);
        }
        val labEntry = labEntries[position]
        holder.bind(labEntry)
    }

    override fun getItemCount(): Int {
        return labEntries.size
    }

    inner class LabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val labNameTextView: TextView = itemView.findViewById(R.id.labNameTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(labEntry: HistoryActivity.FileEntry) {
            labNameTextView.text = labEntry.labName
            dateTextView.text = labEntry.date
        }
    }
}
