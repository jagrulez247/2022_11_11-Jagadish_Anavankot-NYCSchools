package com.education.nycschools.navigation

import android.os.Bundle
import androidx.activity.viewModels
import com.education.nycschools.R
import com.education.nycschools.schoolinfo.ui.main.NycSchoolInfoFragment
import com.education.nycschools.uicomponents.base.BaseActivity
import com.education.nycschools.uicomponents.base.BaseFragmentData
import com.education.nycschools.uicomponents.base.BaseFragmentData.Companion.FRAGMENT_DATA
import com.education.nycschools.uicomponents.extensions.findExistingFragment
import com.education.nycschools.uicomponents.extensions.replaceFragment
import com.education.nycschools.uicomponents.feature.MainNavSharedViewModel
import com.education.nycschools.uicomponents.feature.data.NycSchoolInfoScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNavActivity: BaseActivity() {

    private val mainNavViewModel: MainNavSharedViewModel by viewModels()
    private var fragmentData: BaseFragmentData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_main)
        fragmentData = intent.getParcelableExtra(FRAGMENT_DATA)
        handleSatItemSelection()
        replaceFragment(
            this,
            containerId = R.id.mainNavContainer,
            fragment = MainNavFragmentFactory.getNavFragment(fragmentData),
            data = fragmentData ?: NycSchoolInfoScreen().setEnterAnimation(
                com.education.nycschools.uicomponents.R.anim.anim_slide_right_to_left
            )
        )
    }

    private fun handleSatItemSelection() {
        mainNavViewModel
            .listenForSatItemSelection()
            .observeForever { dbn ->
                val tagName = fragmentData?.tagRes?.takeIf { it > 0 }?.let { getString(it) }
                findExistingFragment(tagName)
                    ?.let { it as? NycSchoolInfoFragment }
                    ?.onSatItemSelected(dbn)
            }
    }
}