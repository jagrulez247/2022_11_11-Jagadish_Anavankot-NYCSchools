package com.education.nycschools.schoolinfo.ui.sats.adapter

import androidx.recyclerview.widget.DiffUtil
import com.education.nycschools.domain.models.NycSchoolSatData

internal class NycSchoolSatDiff(
    private val oldList: List<NycSchoolSatData>,
    private val newList: List<NycSchoolSatData>
) : DiffUtil.Callback() {

    companion object {
        fun refreshItemUi(
            adapter: NycSchoolSatAdapter,
            oldItems: MutableList<NycSchoolSatData>,
            newItems: List<NycSchoolSatData>,
        ) {
            val diffResult = DiffUtil.calculateDiff(NycSchoolSatDiff(oldItems, newItems))
            oldItems.clear()
            oldItems.addAll(newItems)
            diffResult.dispatchUpdatesTo(adapter)
        }
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItemData = oldList[oldItemPosition]
        val newItemData = newList[newItemPosition]
        return oldItemData.dbn == newItemData.dbn
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItemData = oldList[oldItemPosition]
        val newItemData = newList[newItemPosition]
        return oldItemData == newItemData
    }
}