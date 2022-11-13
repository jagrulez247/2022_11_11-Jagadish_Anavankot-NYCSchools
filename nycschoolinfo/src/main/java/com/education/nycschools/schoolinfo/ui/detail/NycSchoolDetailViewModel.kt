package com.education.nycschools.schoolinfo.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.nycschools.common.utils.LocalCoroutineDispatcher.main
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.schoolinfo.R
import com.education.nycschools.schoolinfo.ui.detail.NycSchoolDetailUiStates.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NycSchoolDetailViewModel @Inject constructor(
    private val repository: NycSchoolsInfoRepository
) : ViewModel() {

    private val uiState: MutableLiveData<NycSchoolDetailUiStates> = MutableLiveData()
    internal fun uiState(): LiveData<NycSchoolDetailUiStates> = uiState

    fun onViewCreated(context: Context, dbn: String) {
        viewModelScope.launch(main()) {
            repository.fetchSchoolData(dbn)
                .onStart { emit(DataFetchResult.loading()) }
                .collect { result ->
                    val isLoading = result?.status == DataFetchResult.Status.LOADING
                    if (isLoading) {
                        uiState.value = Loading
                        return@collect
                    }

                    val unavailable = context.getString(
                        com.education.nycschools.uicomponents.R.string.nyc_school_info_unavailable
                    )
                    val schoolData = result?.data
                    val schoolName = schoolData?.school_name ?: ""
                    val totalStudents = String.format(
                        context.getString(R.string.nyc_school_detail_total_students),
                        schoolData?.total_students ?: unavailable
                    )
                    val gradRate = schoolData
                        ?.graduation_rate
                        ?.toDoubleOrNull()
                        ?.let { it * 100 }
                        ?.let { "%.2f".format(it) }
                        ?.let {
                            String.format(
                                context.getString(R.string.nyc_school_detail_graduation_rate),
                                it
                            )
                        }
                        ?: String.format(
                            context.getString(R.string.nyc_school_detail_graduation_rate),
                            unavailable
                        )
                    val overview = schoolData?.overview_paragraph ?: ""
                    uiState.value = if (schoolName.isBlank()) NoData else Loaded
                    uiState.value = SchoolName(schoolName)
                    uiState.value = SchoolTotalStudents(totalStudents)
                    uiState.value = SchoolGradRate(gradRate)
                    uiState.value = SchoolDescription(overview)
                }
        }
    }
}