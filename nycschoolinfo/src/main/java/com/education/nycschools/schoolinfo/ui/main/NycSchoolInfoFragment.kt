package com.education.nycschools.schoolinfo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.fragment.app.activityViewModels
import com.education.nycschools.schoolinfo.R
import com.education.nycschools.schoolinfo.databinding.FragmentNycSchoolsInfoBinding
import com.education.nycschools.schoolinfo.ui.detail.NycSchoolDetailFragment
import com.education.nycschools.schoolinfo.ui.sats.NycSchoolSatsFragment
import com.education.nycschools.uicomponents.base.BaseFragment
import com.education.nycschools.uicomponents.extensions.observe
import com.education.nycschools.uicomponents.extensions.replaceFragment
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
    private lateinit var detailFragment: NycSchoolDetailFragment

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
        replaceFragment(
            context,
            R.id.nycSchoolSatsFragmentContainer,
            NycSchoolSatsFragment.getNycSchoolSatsFragment(),
            NycSchoolSatsScreen()
        )
        viewModel.onViewCreated()
    }

    internal fun onSatItemSelected(dbn: String) {
        viewModel.onSatItemSelected(dbn)
    }

    private fun updateUi(state: NycSchoolInfoUiStates) {
        when (state) {
            is NycSchoolInfoUiStates.LoadSchoolDetail -> {

            }
            NycSchoolInfoUiStates.Loaded -> {
                binding.nycSchoolInfoProgressBar.visibility = GONE
                binding.nycSchoolSatsFragmentContainer.visibility = VISIBLE
                binding.nycSchoolDetailFragmentContainer.visibility = VISIBLE
            }
            NycSchoolInfoUiStates.Loading -> {
                binding.nycSchoolInfoProgressBar.visibility = VISIBLE
                binding.nycSchoolSatsFragmentContainer.visibility = GONE
                binding.nycSchoolDetailFragmentContainer.visibility = GONE
            }
            NycSchoolInfoUiStates.NoData -> binding.nycSchoolInfoNoData.visibility = VISIBLE
            is NycSchoolInfoUiStates.LoadError -> context?.let {
                Toast.makeText(it, R.string.nyc_school_data_fetch_error, LENGTH_LONG).show()
            }
        }
    }
}