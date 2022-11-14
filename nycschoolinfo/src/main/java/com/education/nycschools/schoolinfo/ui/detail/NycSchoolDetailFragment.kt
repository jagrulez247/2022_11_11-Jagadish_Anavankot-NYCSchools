package com.education.nycschools.schoolinfo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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

    private val viewModel: NycSchoolDetailViewModel by viewModels()
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
            is HideEligibility -> {
                binding.nycSchoolDetailContent.nycSchoolEligibility.visibility = GONE
            }
            is HideAddress -> {
                binding.nycSchoolDetailContent.nycSchoolAddress.visibility = GONE
            }
            is HideEmail -> {
                binding.nycSchoolDetailContent.nycSchoolEmail.visibility = GONE
            }
            is HidePhone -> {
                binding.nycSchoolDetailContent.nycSchoolPhone.visibility = GONE
            }
            is HideAcademics -> {
                binding.nycSchoolDetailContent.nycSchoolHighlights.visibility = GONE
                binding.nycSchoolDetailContent.nycSchoolHighlightsDetail.visibility = GONE
            }
            is HideActivities -> {
                binding.nycSchoolDetailContent.nycSchoolActivities.visibility = GONE
                binding.nycSchoolDetailContent.nycSchoolActivitiesDetail.visibility = GONE
            }
            is HideSports -> {
                binding.nycSchoolDetailContent.nycSchoolSports.visibility = GONE
                binding.nycSchoolDetailContent.nycSchoolSportsDetail.visibility = GONE
            }
            is SchoolEligibility -> {
                binding.nycSchoolDetailContent.nycSchoolEligibility.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolEligibility.text = state.eligibility
            }
            is SchoolAddress -> {
                binding.nycSchoolDetailContent.nycSchoolAddress.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolAddress.text = state.address
            }
            is SchoolEmail -> {
                binding.nycSchoolDetailContent.nycSchoolEmail.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolEmail.text = state.email
            }
            is SchoolPhone -> {
                binding.nycSchoolDetailContent.nycSchoolPhone.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolPhone.text = state.phone
            }
            is SchoolAcademics -> {
                binding.nycSchoolDetailContent.nycSchoolHighlights.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolHighlightsDetail.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolHighlightsDetail.text = state.academics
            }
            is SchoolActivities -> {
                binding.nycSchoolDetailContent.nycSchoolActivities.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolActivitiesDetail.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolActivitiesDetail.text = state.activities
            }
            is SchoolSports -> {
                binding.nycSchoolDetailContent.nycSchoolSports.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolSportsDetail.visibility = VISIBLE
                binding.nycSchoolDetailContent.nycSchoolSportsDetail.text = state.sports
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