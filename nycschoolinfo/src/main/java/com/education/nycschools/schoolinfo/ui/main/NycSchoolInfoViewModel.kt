package com.education.nycschools.schoolinfo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.nycschools.common.utils.LocalCoroutineDispatcher.main
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.schoolinfo.ui.main.NycSchoolInfoUiStates.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NycSchoolInfoViewModel @Inject constructor(
    private val repository: NycSchoolsInfoRepository
): ViewModel() {

    private val uiState: MutableLiveData<NycSchoolInfoUiStates> = MutableLiveData()
    internal fun uiState(): LiveData<NycSchoolInfoUiStates> = uiState

    fun onViewCreated() {
        viewModelScope.launch(main()) {
            repository.fetchAllSchoolSats()
                .onStart { emit(DataFetchResult.loading()) }
                .collect { result ->
                    val isLoading = result?.status == DataFetchResult.Status.LOADING
                    if (isLoading) {
                        uiState.value = Loading
                        return@collect
                    }

                    val schoolSats = result
                        ?.data
                        ?.filter { satData -> satData.school_name != null }
                        ?.sortedBy { satData -> satData.school_name }
                        ?: mutableListOf()
                    uiState.value = if (schoolSats.isEmpty()) NoData else Loaded
                }
        }
    }

    fun onSatItemSelected(dbn: String) {
        viewModelScope.launch(main()) { uiState.value = LoadSchoolDetail(dbn) }
    }
}