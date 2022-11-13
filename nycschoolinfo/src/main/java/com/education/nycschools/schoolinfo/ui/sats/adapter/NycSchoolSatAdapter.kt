package com.education.nycschools.schoolinfo.ui.sats.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.education.nycschools.domain.models.NycSchoolSatData
import com.education.nycschools.schoolinfo.R

internal class NycSchoolSatAdapter(
    private val onItemClick: (String, Int) -> Unit
) : RecyclerView.Adapter<NycSchoolSatItemViewHolder>() {

    private val satItems: MutableList<NycSchoolSatData> = mutableListOf()
    private var unavailableText = ""
    private var mathText = ""
    private var readingText = ""
    private var writingText = ""
    private var totalTestsText = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NycSchoolSatItemViewHolder {
        unavailableText = parent.context.getString(
            com.education.nycschools.uicomponents.R.string.nyc_school_info_unavailable
        )
        mathText = parent.context.getString(R.string.nyc_school_sat_math)
        readingText = parent.context.getString(R.string.nyc_school_sat_reading)
        writingText = parent.context.getString(R.string.nyc_school_sat_writing)
        totalTestsText = parent.context.getString(R.string.nyc_school_sat_total_takes)
        return NycSchoolSatItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: NycSchoolSatItemViewHolder, position: Int) {
        val satItem = satItems[position]
        holder.setName(satItem.school_name ?: unavailableText)
        holder.setMathScore(
            String.format(
                mathText,
                satItem.sat_math_avg_score
                    ?.takeIf { it.toIntOrNull() != null }
                    ?: unavailableText
            )
        )
        holder.setReadingScore(
            String.format(
                readingText,
                satItem.sat_critical_reading_avg_score
                    ?.takeIf { it.toIntOrNull() != null }
                    ?: unavailableText
            )
        )
        holder.setWritingScore(
            String.format(
                writingText,
                satItem.sat_writing_avg_score
                    ?.takeIf { it.toIntOrNull() != null }
                    ?: unavailableText
            )
        )
        holder.setTotalTests(
            String.format(
                totalTestsText,
                satItem.num_of_sat_test_takers
                    ?.takeIf { it.toIntOrNull() != null }
                    ?: unavailableText
            )
        )
        holder.setSelected(satItem.selected ?: false)
        holder.itemView.setOnClickListener {
            onItemClick(satItem.dbn, position)
        }
    }

    override fun getItemCount(): Int = satItems.size

    fun update(newItems: List<NycSchoolSatData>) {
        NycSchoolSatDiff.refreshItemUi(this, satItems, newItems)
    }
}