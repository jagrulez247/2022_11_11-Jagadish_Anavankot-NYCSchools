package com.education.nycschools.schoolinfo.ui.detail

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.education.nycschools.uicomponents.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NycSchoolDetailFragment: BaseFragment() {

    companion object {

        fun getNycSchoolDetailFragment(
            bundle: Bundle? = null
        ) = NycSchoolDetailFragment().apply { arguments = bundle ?: Bundle() }
    }

    private val viewModel: NycSchoolDetailViewModel by activityViewModels()
}