package com.education.nycschools.schoolinfo.ui.sats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.education.nycschools.schoolinfo.databinding.FragmentNycSchoolSatsBinding
import com.education.nycschools.schoolinfo.ui.sats.adapter.NycSchoolSatAdapter
import com.education.nycschools.uicomponents.base.BaseFragment
import com.education.nycschools.uicomponents.extensions.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NycSchoolSatsFragment : BaseFragment() {

    companion object {

        fun getNycSchoolSatsFragment(
            bundle: Bundle? = null
        ) = NycSchoolSatsFragment().apply { arguments = bundle ?: Bundle() }
    }

    private val viewModel: NycSchoolSatsViewModel by activityViewModels()
    private lateinit var binding: FragmentNycSchoolSatsBinding
    private lateinit var satAdapter: NycSchoolSatAdapter
    private lateinit var itemSelectedCallback: (String) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNycSchoolSatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView(view)
        setupSearchView()
        observe(viewModel.uiState()) { updateUi(it) }
        viewModel.onViewCreated()
    }

    internal fun setItemSelectedListener(callback: (String) -> Unit) {
        itemSelectedCallback = callback
    }

    private fun updateUi(state: NycSchoolSatsUiStates) {
        when (state) {
            is NycSchoolSatsUiStates.UpdateSchoolSats -> satAdapter.update(state.sats)
            is NycSchoolSatsUiStates.InformItemSelection -> itemSelectedCallback(state.dbn)
            is NycSchoolSatsUiStates.DismissKeyboard -> binding.searchLayout.hideKeyboard()
            is NycSchoolSatsUiStates.HideNoData -> {
                binding.nycSchoolSatsNoSearchData.visibility = GONE
            }
            is NycSchoolSatsUiStates.ShowNoData -> {
                binding.nycSchoolSatsNoSearchData.visibility = VISIBLE
            }
        }
    }

    private fun setRecyclerView(view: View) {
        binding.nycSchoolSatsRecyclerView.apply {
            layoutManager = LinearLayoutManager(view.context, RecyclerView.HORIZONTAL, false)
            LinearSnapHelper().attachToRecyclerView(this)
            adapter = NycSchoolSatAdapter(
                onItemClick = { dbn, position ->
                    binding.nycSchoolSatsRecyclerView.smoothScrollToPosition(position)
                    viewModel.onItemSelected(dbn)
                }
            ).apply { satAdapter = this }
        }
    }

    private fun setupSearchView() {
        binding.searchLayout.listenToQueryChanges(
            lifecycle = viewLifecycleOwner.lifecycle,
            searchCallback = { viewModel.onSearchQuery(it) }
        )
    }
}