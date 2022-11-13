package com.education.nycschools.schoolinfo.ui.sats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.education.nycschools.uicomponents.R
import com.education.nycschools.schoolinfo.databinding.ViewHolderNycSchoolSatBinding

internal class NycSchoolSatItemViewHolder(
    parent: ViewGroup,
    private val itemBinding: ViewHolderNycSchoolSatBinding = getWeatherItemBinding(parent)
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun setName(name: String) {
        itemBinding.nycSchoolName.text = name
    }

    fun setMathScore(score: String) {
        itemBinding.nycSchoolSatMath.text = score
    }

    fun setWritingScore(score: String) {
        itemBinding.nycSchoolSatWriting.text = score
    }

    fun setReadingScore(score: String) {
        itemBinding.nycSchoolSatReading.text = score
    }

    fun setTotalTests(total: String) {
        itemBinding.nycSchoolSatTotalTests.text = total
    }

    fun setSelected(isSelected: Boolean) {
        val res = if (isSelected)
            R.drawable.rounded_button_tertiary_variant_with_border_view
        else
            R.drawable.rounded_primary_with_border_view
        itemBinding.nycSchoolSatCard.setBackgroundResource(res)
    }

    companion object {
        private fun getWeatherItemBinding(parent: ViewGroup) =
            ViewHolderNycSchoolSatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
    }
}