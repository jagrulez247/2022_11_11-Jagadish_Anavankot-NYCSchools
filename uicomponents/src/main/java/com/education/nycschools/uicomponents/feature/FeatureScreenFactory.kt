package com.education.nycschools.uicomponents.feature

import android.content.Context
import com.education.nycschools.uicomponents.R
import com.education.nycschools.uicomponents.base.BaseFragmentData
import com.education.nycschools.uicomponents.feature.data.NycSchoolDetailScreen
import com.education.nycschools.uicomponents.feature.data.NycSchoolInfoScreen
import com.education.nycschools.uicomponents.feature.data.NycSchoolSatsScreen

object FeatureScreenFactory {

    fun getMainNavFragmentData(context: Context, tag: String?): BaseFragmentData {
        return when (tag) {
            context.getString(R.string.nyc_school_info_fragment) -> NycSchoolInfoScreen()
            context.getString(R.string.nyc_school_sats_fragment) -> NycSchoolSatsScreen()
            context.getString(R.string.nyc_school_detail_fragment) -> NycSchoolDetailScreen()
            else -> NycSchoolInfoScreen()
        }
    }
}