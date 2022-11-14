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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NycSchoolInfoViewModel @Inject constructor(
    private val repository: NycSchoolsInfoRepository
): ViewModel() {

    private val uiState: MutableLiveData<NycSchoolInfoUiStates> = MutableLiveData()
    internal fun uiState(): LiveData<NycSchoolInfoUiStates> = uiState

    companion object {
        private const val TAG = "NycSchoolInfoViewModel"
    }

    private val prefetchScope by lazy { CoroutineScope(SupervisorJob() + main()) }
    private var prefetchJob: Job? = null

    fun onViewCreated() {
        prefetchJob = prefetchScope.launch(main()) {
            repository.fetchAllSchoolSats().cancellable().collect { result ->
                val isLoading = result?.status == DataFetchResult.Status.LOADING
                val schoolSats = result?.data ?: mutableListOf()
                if (schoolSats.isNotEmpty()) {
                    handleLoadedScenario()
                    prefetchJob?.cancel()
                    return@collect
                }

                when {
                    isLoading -> handleLoadingScenario()
                    else -> handleNoDataScenario()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        prefetchJob?.cancel()
    }

    private fun handleLoadedScenario() {
        viewModelScope.launch(main()) { uiState.value = Loaded }
    }

    private fun handleLoadingScenario() {
        viewModelScope.launch(main()) { uiState.value = Loading }
    }

    private fun handleNoDataScenario() {
        viewModelScope.launch(main()) { uiState.value = NoData }
    }

    fun onSatItemSelected(dbn: String) {
        viewModelScope.launch(main()) { uiState.value = LoadSchoolDetail(dbn) }
    }
}