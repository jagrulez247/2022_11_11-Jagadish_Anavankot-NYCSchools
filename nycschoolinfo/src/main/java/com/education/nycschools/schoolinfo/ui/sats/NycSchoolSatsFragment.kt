package com.education.nycschools.schoolinfo.ui.sats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.education.nycschools.schoolinfo.databinding.FragmentNycSchoolSatsBinding
import com.education.nycschools.schoolinfo.ui.sats.adapter.NycSchoolSatAdapter
import com.education.nycschools.uicomponents.base.BaseFragment
import com.education.nycschools.uicomponents.extensions.observe
import com.education.nycschools.uicomponents.feature.MainNavSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NycSchoolSatsFragment : BaseFragment() {

    companion object {

        fun getNycSchoolSatsFragment(
            bundle: Bundle? = null
        ) = NycSchoolSatsFragment().apply { arguments = bundle ?: Bundle() }
    }

    private val viewModel: NycSchoolSatsViewModel by viewModels()
    private val mainNavViewModel: MainNavSharedViewModel by activityViewModels()
    private lateinit var binding: FragmentNycSchoolSatsBinding
    private lateinit var satAdapter: NycSchoolSatAdapter

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

    private fun updateUi(state: NycSchoolSatsUiStates) {
        when (state) {
            is NycSchoolSatsUiStates.UpdateSchoolSats -> satAdapter.update(state.sats)
            is NycSchoolSatsUiStates.InformItemSelection ->{
                mainNavViewModel.satListItemSelected(state.dbn)
            }
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
            val layoutManagerProperty = layoutManager
            LinearSnapHelper().attachToRecyclerView(this)
            adapter = NycSchoolSatAdapter(
                onItemClick = { dbn, position ->
                    binding.nycSchoolSatsRecyclerView.smoothScrollToPosition(position)
                    viewModel.onItemSelected(dbn)
                }
            ).apply { satAdapter = this }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val visiblePos = (layoutManagerProperty as? LinearLayoutManager)
                            ?.findFirstCompletelyVisibleItemPosition() ?: -1
                        val itemDbn = if (visiblePos != -1) {
                            (adapter as? NycSchoolSatAdapter)?.getDbnAtPos(visiblePos) ?: ""
                        } else {
                            ""
                        }
                        if (itemDbn.isNotBlank()) {
                            binding.nycSchoolSatsRecyclerView.smoothScrollToPosition(visiblePos)
                            viewModel.onItemSelected(itemDbn)
                        }
                    }
                }
            })
        }
    }

    private fun setupSearchView() {
        binding.searchLayout.listenToQueryChanges(
            lifecycle = viewLifecycleOwner.lifecycle,
            searchCallback = { viewModel.onSearchQuery(it) }
        )
    }
}