package com.education.nycschools.schoolinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.education.nycschools.schoolinfo.databinding.FragmentNycSchoolsInfoBinding
import com.education.nycschools.uicomponents.base.BaseFragment

class NycSchoolInfoFragment: BaseFragment() {

    companion object {

        fun getNycSchoolInfoFragment(
            bundle: Bundle? = null
        ) = NycSchoolInfoFragment().apply { arguments = bundle ?: Bundle() }
    }

    private val viewModel: NycSchoolInfoViewModel by activityViewModels()
    private lateinit var binding: FragmentNycSchoolsInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNycSchoolsInfoBinding.inflate(inflater, container, false)
        return binding.root
    }
}