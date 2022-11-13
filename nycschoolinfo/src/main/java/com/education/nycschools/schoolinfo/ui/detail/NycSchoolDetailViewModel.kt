package com.education.nycschools.schoolinfo.ui.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.nycschools.common.utils.LocalCoroutineDispatcher.main
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.domain.models.NycSchoolData
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
                    val email = schoolData?.school_email ?: ""
                    val phone = schoolData?.phone_number ?: ""
                    val address = getAddress(schoolData)
                    val gradRate = getGradRate(context, schoolData)
                    val academics = getFullAcademicString(context, schoolData)
                    val eligible = schoolData?.eligibility1 ?: ""
                    val overview = schoolData?.overview_paragraph ?: ""
                    val activities = getActivities(schoolData)
                    val sports = getSports(schoolData)
                    uiState.value = if (schoolName.isBlank()) NoData else Loaded
                    uiState.value = SchoolName(schoolName)
                    uiState.value =
                        if (eligible.isBlank()) HideEligibility else SchoolEligibility(eligible)
                    uiState.value =
                        if (address.isBlank()) HideAddress else SchoolAddress(address)
                    uiState.value = SchoolTotalStudents(totalStudents)
                    uiState.value = SchoolGradRate(gradRate)
                    uiState.value = SchoolDescription(overview)
                    uiState.value =
                        if (academics.isBlank()) HideAcademics else SchoolAcademics(academics)
                    uiState.value =
                        if (activities.isBlank()) HideActivities else SchoolActivities(activities)
                    uiState.value =
                        if (sports.isBlank()) HideSports else SchoolSports(sports)
                    uiState.value =
                        if (email.isBlank()) HideEmail else SchoolEmail(email)
                    uiState.value =
                        if (phone.isBlank()) HidePhone else SchoolPhone(phone)
                }
        }
    }

    private fun getSports(schoolData: NycSchoolData?): String {
        return "${schoolData?.psal_sports_boys?.plus(", ") ?: ""}${schoolData?.psal_sports_girls ?: ""}"
    }

    private fun getActivities(schoolData: NycSchoolData?): String {
        return "${schoolData?.addtl_info1?.plus(", ") ?: ""}${schoolData?.extracurricular_activities ?: ""}"
    }

    private fun getAddress(schoolData: NycSchoolData?): String {
        return "${
            schoolData?.primary_address_line_1?.plus(" ") ?: ""
        }${
            schoolData?.city?.plus(" ") ?: ""
        }${
            schoolData?.state_code?.plus(" ") ?: ""
        }${
            schoolData?.zip?.plus(" ") ?: ""
        }"
    }

    private fun getGradRate(context: Context, schoolData: NycSchoolData?): String {
        val unavailable = context.getString(
            com.education.nycschools.uicomponents.R.string.nyc_school_info_unavailable
        )
        return schoolData
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
    }

    private fun getFullAcademicString(context: Context, schoolData: NycSchoolData?): String {
        return "${
            getAcademicString(
                context,
                schoolData?.academicopportunities1
            )
        }${
            getAcademicString(
                context,
                schoolData?.academicopportunities2
            )
        }${
            getAcademicString(
                context,
                schoolData?.academicopportunities3
            )
        }${
            getAcademicString(
                context,
                schoolData?.academicopportunities4
            )
        }${
            getAcademicString(
                context,
                schoolData?.academicopportunities5
            )
        }"
    }

    private fun getAcademicString(context: Context, academicString: String?): String {
        if (academicString.isNullOrBlank()) return ""
        return String.format(
            context.getString(R.string.nyc_school_detail_highlights_detail),
            academicString.plus("\n")
        )
    }
}