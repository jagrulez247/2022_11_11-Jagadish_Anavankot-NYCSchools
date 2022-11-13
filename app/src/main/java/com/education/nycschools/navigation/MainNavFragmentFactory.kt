package com.education.nycschools.navigation

import com.education.nycschools.schoolinfo.ui.detail.NycSchoolDetailFragment
import com.education.nycschools.schoolinfo.ui.main.NycSchoolInfoFragment
import com.education.nycschools.schoolinfo.ui.sats.NycSchoolSatsFragment
import com.education.nycschools.uicomponents.base.BaseFragment
import com.education.nycschools.uicomponents.base.BaseFragmentData
import com.education.nycschools.uicomponents.feature.data.NycSchoolDetailScreen
import com.education.nycschools.uicomponents.feature.data.NycSchoolInfoScreen
import com.education.nycschools.uicomponents.feature.data.NycSchoolSatsScreen

object MainNavFragmentFactory {
    fun getNavFragment(data: BaseFragmentData?): BaseFragment{
        return when (data) {
            is NycSchoolInfoScreen -> {
                NycSchoolInfoFragment.getNycSchoolInfoFragment(bundle = data.build())
            }
            is NycSchoolSatsScreen -> {
                NycSchoolSatsFragment.getNycSchoolSatsFragment(bundle = data.build())
            }
            is NycSchoolDetailScreen -> {
                NycSchoolDetailFragment.getNycSchoolDetailFragment(bundle = data.build())
            }
            else -> NycSchoolInfoFragment.getNycSchoolInfoFragment()
        }
    }
}