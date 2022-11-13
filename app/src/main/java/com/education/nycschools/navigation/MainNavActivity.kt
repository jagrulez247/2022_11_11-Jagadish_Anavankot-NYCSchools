package com.education.nycschools.navigation

import android.os.Bundle
import com.education.nycschools.R
import com.education.nycschools.uicomponents.base.BaseActivity
import com.education.nycschools.uicomponents.base.BaseFragmentData
import com.education.nycschools.uicomponents.base.BaseFragmentData.Companion.FRAGMENT_DATA
import com.education.nycschools.uicomponents.extensions.replaceFragment
import com.education.nycschools.uicomponents.feature.data.NycSchoolInfoScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNavActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_main)
        val fragmentData = intent.getParcelableExtra<BaseFragmentData>(FRAGMENT_DATA)
        val fragment = MainNavFragmentFactory.getNavFragment(fragmentData)
        replaceFragment(
            this,
            containerId = R.id.mainNavContainer,
            fragment = fragment,
            data = fragmentData ?: NycSchoolInfoScreen()
        )
    }
}