package com.education.nycschools.schoolinfo.ui.sats

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.nycschools.common.utils.LocalCoroutineDispatcher.main
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import com.education.nycschools.domain.models.NycSchoolSatData
import com.education.nycschools.schoolinfo.ui.sats.NycSchoolSatsUiStates.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NycSchoolSatsViewModel @Inject constructor(
    private val repository: NycSchoolsInfoRepository
) : ViewModel() {

    private val uiState: MutableLiveData<NycSchoolSatsUiStates> = MutableLiveData()
    internal fun uiState(): LiveData<NycSchoolSatsUiStates> = uiState
    internal val schoolSats: MutableList<NycSchoolSatData> = mutableListOf()
    private val searchSats: MutableList<NycSchoolSatData> = mutableListOf()
    private var searchQuery: String = ""

    fun onViewCreated() {
        viewModelScope.launch(main()) {
            val cachedSchoolSats = repository.getSatsFromCache()
            if (cachedSchoolSats.isNotEmpty()) {
                handleSatData(cachedSchoolSats)
                return@launch
            }

            repository.fetchAllSchoolSats().collect { result -> handleSatData(result?.data) }
        }
    }

    @MainThread
    private fun handleSatData(satDataFetched: List<NycSchoolSatData>?) {
        val schoolSatsFetched = satDataFetched
            ?.filter { satData -> satData.school_name != null }
            ?.sortedBy { satData -> satData.school_name }
            ?: mutableListOf()
        schoolSats.clear()
        schoolSats.addAll(schoolSatsFetched)
        val firstDbn = schoolSats.takeIf { it.isNotEmpty() }?.get(0)?.dbn ?: ""
        uiState.value = if (schoolSats.isEmpty()) ShowNoData else HideNoData
        if (firstDbn.isNotBlank()) {
            handleItemSelected(firstDbn)
        } else {
            uiState.value = UpdateSchoolSats(schoolSats)
        }
    }

    fun onItemSelected(dbn: String?) {
        viewModelScope.launch(main()) { handleItemSelected(dbn) }
    }

    fun onSearchQuery(query: String?) {
        if (searchQuery == query) return
        searchQuery = query?.trim() ?: ""
        viewModelScope.launch(main()) {
            if (searchQuery.isBlank()) {
                uiState.value = if (schoolSats.isEmpty()) ShowNoData else HideNoData
                uiState.value = UpdateSchoolSats(schoolSats)
                return@launch
            }

            val searchResults = schoolSats
                .filter {
                    it.school_name?.lowercase(Locale.getDefault())?.contains(
                        searchQuery.lowercase(
                            Locale.getDefault()
                        )
                    ) == true
                }
                .map { it.copy() }
                .sortedBy { it.school_name }
            searchSats.clear()
            searchSats.addAll(searchResults)
            uiState.value = if (searchSats.isEmpty()) ShowNoData else HideNoData
            uiState.value = UpdateSchoolSats(searchSats)
        }
    }

    private fun handleItemSelected(dbn: String?) {
        if (dbn.isNullOrBlank()) return
        updateSatsAfterSelection(dbn, schoolSats)
        updateSatsAfterSelection(dbn, searchSats)
        val satList = if (searchQuery.isBlank()) schoolSats else searchSats
        uiState.value = UpdateSchoolSats(satList)
        uiState.value = InformItemSelection(dbn)
        uiState.value = DismissKeyboard
    }

    private fun updateSatsAfterSelection(dbn: String, satList: MutableList<NycSchoolSatData>) {
        val selectionIndex = satList.indexOfFirst { it.dbn == dbn }
        if (selectionIndex == -1) return
        for (index in satList.indices) {
            val itemAtIndex = satList[index]
            satList[index] = itemAtIndex.copy(selected = index == selectionIndex)
        }
    }
}