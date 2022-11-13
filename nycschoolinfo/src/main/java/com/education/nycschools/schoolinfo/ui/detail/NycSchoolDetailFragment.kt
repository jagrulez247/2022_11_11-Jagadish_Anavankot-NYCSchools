package com.education.nycschools.schoolinfo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.education.nycschools.schoolinfo.databinding.FragmentNycSchoolDetailBinding
import com.education.nycschools.uicomponents.base.BaseFragment
import com.education.nycschools.uicomponents.extensions.observe
import com.education.nycschools.schoolinfo.ui.detail.NycSchoolDetailUiStates.*
import com.education.nycschools.uicomponents.feature.FeatureConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NycSchoolDetailFragment: BaseFragment() {

    companion object {

        fun getNycSchoolDetailFragment(
            bundle: Bundle? = null
        ) = NycSchoolDetailFragment().apply { arguments = bundle ?: Bundle() }
    }

    private val viewModel: NycSchoolDetailViewModel by activityViewModels()
    private lateinit var binding: FragmentNycSchoolDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNycSchoolDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbn = arguments?.getString(FeatureConstants.NYC_SCHOOL_DBN, "") ?: ""
        observe(viewModel.uiState()) { updateUi(it) }
        viewModel.onViewCreated(view.context, dbn)
    }

    private fun updateUi(state: NycSchoolDetailUiStates) {
        when (state) {
            is SchoolDescription -> {
                binding.nycSchoolDetailContent.nycSchoolDescription.text = state.overview
            }
            is SchoolGradRate -> {
                binding.nycSchoolDetailContent.nycSchoolGraduationRate.text = state.rate
            }
            is SchoolName -> binding.nycSchoolDetailContent.nycSchoolName.text = state.name
            is SchoolTotalStudents -> {
                binding.nycSchoolDetailContent.nycSchoolTotalStudents.text = state.total
            }
            is Loaded -> {
                binding.nycSchoolDetailNoData.visibility = GONE
                binding.nycSchoolDetailProgressBar.visibility = GONE
                binding.nycSchoolDetailContent.itemDetailContentScrollView.visibility = VISIBLE
            }
            is Loading -> {
                binding.nycSchoolDetailProgressBar.visibility = VISIBLE
                binding.nycSchoolDetailNoData.visibility = GONE
                binding.nycSchoolDetailContent.itemDetailContentScrollView.visibility = GONE
            }
            is NoData -> {
                binding.nycSchoolDetailProgressBar.visibility = GONE
                binding.nycSchoolDetailNoData.visibility = VISIBLE
            }
        }
    }
}