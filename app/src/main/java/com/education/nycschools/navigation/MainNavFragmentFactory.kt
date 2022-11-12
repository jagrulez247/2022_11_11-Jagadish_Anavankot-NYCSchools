package com.education.nycschools.navigation

import com.education.nycschools.schoolinfo.ui.NycSchoolInfoFragment
import com.education.nycschools.uicomponents.base.BaseFragment
import com.education.nycschools.uicomponents.base.BaseFragmentData
import com.education.nycschools.uicomponents.feature.data.NycSchoolInfoScreen

object MainNavFragmentFactory {
    fun getNavFragment(data: BaseFragmentData?): BaseFragment{
        // Only one screen atm
        return when (data) {
            is NycSchoolInfoScreen -> {
                NycSchoolInfoFragment.getNycSchoolInfoFragment(bundle = data.build())
            }
            else -> NycSchoolInfoFragment.getNycSchoolInfoFragment()
        }
    }
}