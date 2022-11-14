package com.education.nycschools.schoolinfo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.education.nycschools.schoolinfo.R
import com.education.nycschools.schoolinfo.databinding.FragmentNycSchoolsInfoBinding
import com.education.nycschools.schoolinfo.ui.detail.NycSchoolDetailFragment
import com.education.nycschools.schoolinfo.ui.sats.NycSchoolSatsFragment
import com.education.nycschools.uicomponents.base.BaseFragment
import com.education.nycschools.uicomponents.extensions.observe
import com.education.nycschools.uicomponents.extensions.replaceFragment
import com.education.nycschools.uicomponents.feature.FeatureConstants.NYC_SCHOOL_DBN
import com.education.nycschools.uicomponents.feature.data.NycSchoolDetailScreen
import com.education.nycschools.uicomponents.feature.data.NycSchoolSatsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NycSchoolInfoFragment : BaseFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.uiState()) { updateUi(it) }
        viewModel.onViewCreated()
        replaceFragment(
            context,
            R.id.nycSchoolSatsFragmentContainer,
            NycSchoolSatsFragment
                .getNycSchoolSatsFragment()
                .apply { setItemSelectedListener { viewModel.onSatItemSelected(it) } },
            NycSchoolSatsScreen().setEnterAnimation(
                com.education.nycschools.uicomponents.R.anim.anim_fade_in
            )
        )
    }

    private fun updateUi(state: NycSchoolInfoUiStates) {
        when (state) {
            is NycSchoolInfoUiStates.LoadSchoolDetail -> replaceFragment(
                context,
                R.id.nycSchoolDetailFragmentContainer,
                NycSchoolDetailFragment.getNycSchoolDetailFragment(
                    Bundle().apply { putString(NYC_SCHOOL_DBN, state.dbn) }
                ),
                NycSchoolDetailScreen().setEnterAnimation(
                    com.education.nycschools.uicomponents.R.anim.anim_fade_in
                )
            )
            NycSchoolInfoUiStates.Loaded -> {
                binding.nycSchoolInfoNoData.visibility = GONE
                binding.nycSchoolInfoProgressBar.visibility = GONE
                binding.nycSchoolSatsFragmentContainer.visibility = VISIBLE
                binding.nycSchoolDetailFragmentContainer.visibility = VISIBLE
            }
            NycSchoolInfoUiStates.Loading -> {
                binding.nycSchoolInfoProgressBar.visibility = VISIBLE
                binding.nycSchoolInfoNoData.visibility = GONE
                binding.nycSchoolSatsFragmentContainer.visibility = GONE
                binding.nycSchoolDetailFragmentContainer.visibility = GONE
            }
            NycSchoolInfoUiStates.NoData -> {
                binding.nycSchoolInfoProgressBar.visibility = GONE
                binding.nycSchoolInfoNoData.visibility = VISIBLE
            }
        }
    }
}