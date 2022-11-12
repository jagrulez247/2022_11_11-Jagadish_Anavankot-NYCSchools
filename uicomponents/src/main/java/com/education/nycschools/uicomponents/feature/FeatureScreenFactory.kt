package com.education.nycschools.uicomponents.feature

import android.content.Context
import com.education.nycschools.uicomponents.R
import com.education.nycschools.uicomponents.base.BaseFragmentData
import com.education.nycschools.uicomponents.feature.data.NycSchoolInfoScreen

object FeatureScreenFactory {

    fun getMainNavFragmentData(context: Context, tag: String?): BaseFragmentData {
        // Only one screen atm
        return when (tag) {
            context.getString(R.string.nyc_school_info_fragment) -> NycSchoolInfoScreen()
            else -> NycSchoolInfoScreen()
        }
    }
}