package com.education.nycschools.schoolinfo.ui.sats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.nycschools.common.utils.LocalCoroutineDispatcher
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import com.education.nycschools.domain.models.NycSchoolSatData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NycSchoolSatsViewModel @Inject constructor(
    private val repository: NycSchoolsInfoRepository
): ViewModel() {

    private val uiState: MutableLiveData<NycSchoolSatsUiStates> = MutableLiveData()
    internal fun uiState(): LiveData<NycSchoolSatsUiStates> = uiState
    private val schoolSats: MutableList<NycSchoolSatData> = mutableListOf()

    fun onViewCreated() {
        viewModelScope.launch(LocalCoroutineDispatcher.main()) {
            repository.fetchAllSchoolSats().collect { result ->
                val schoolSatsFetched = result
                    ?.data
                    ?.filter { satData -> satData.school_name != null }
                    ?.sortedBy { satData -> satData.school_name }
                    ?: mutableListOf()
                schoolSats.clear()
                schoolSats.addAll(schoolSatsFetched)
                uiState.value = NycSchoolSatsUiStates.UpdateSchoolSats(schoolSats)
            }
        }
    }

    fun onItemSelected(dbn: String?) {
        if (dbn.isNullOrBlank()) return
        val selectionIndex = schoolSats.indexOfFirst { it.dbn == dbn }
        if (selectionIndex == -1) return
        for (index in schoolSats.indices) {
            val itemAtIndex = schoolSats[index]
            schoolSats[index] = itemAtIndex.copy(selected = index == selectionIndex)
        }
        uiState.value = NycSchoolSatsUiStates.UpdateSchoolSats(schoolSats)
    }
}