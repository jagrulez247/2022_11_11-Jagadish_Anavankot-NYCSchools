package com.education.nycschools.schoolinfo.ui.detail

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.annotation.MainThread
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
import com.education.nycschools.schoolinfo.ui.main.NycSchoolInfoUiStates
import com.education.nycschools.uicomponents.extensions.openMap
import com.education.nycschools.uicomponents.extensions.sendEmailTo
import com.education.nycschools.uicomponents.extensions.sendSmsTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NycSchoolDetailViewModel @Inject constructor(
    private val repository: NycSchoolsInfoRepository
) : ViewModel() {

    private val uiState: MutableLiveData<NycSchoolDetailUiStates> = MutableLiveData()
    internal fun uiState(): LiveData<NycSchoolDetailUiStates> = uiState
    private val underlineSpan = UnderlineSpan()

    private val clickableSpanEmail: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            widget.sendEmailTo(contactEmail)
        }
    }

    private val clickableSpanPhone: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            widget.sendSmsTo(contactPhone)
        }
    }

    private val clickableSpanAddress: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            widget.openMap(contactAddress)
        }
    }

    private var contactEmail: String = ""
    private var contactPhone: String = ""
    private var contactAddress: String = ""

    fun onViewCreated(context: Context, dbn: String) {
        viewModelScope.launch(main()) {
            val cachedSchoolData = repository.getSchoolDetailFromCache(dbn)
            if (cachedSchoolData != null) {
                updateUi(context, cachedSchoolData)
                return@launch
            }

            repository.fetchSchoolData(dbn)
                .collect { result ->
                    val isLoading = result?.status == DataFetchResult.Status.LOADING

                    val schoolName = result?.data?.school_name ?: ""
                    uiState.value = when {
                        schoolName.isNotBlank() -> Loaded
                        isLoading -> Loading
                        else -> NoData
                    }

                    if (schoolName.isBlank()) return@collect

                    updateUi(context, result?.data)
                }
        }
    }

    @MainThread
    private fun updateUi(context: Context, schoolData: NycSchoolData?) {
        uiState.value = Loaded
        val unavailable = context.getString(
            com.education.nycschools.uicomponents.R.string.nyc_school_info_unavailable
        )
        val schoolName = schoolData?.school_name ?: ""
        val totalStudents = String.format(
            context.getString(R.string.nyc_school_detail_total_students),
            schoolData?.total_students ?: unavailable
        )
        contactEmail = schoolData?.school_email ?: ""
        contactPhone = schoolData?.phone_number ?: ""
        contactAddress = getAddress(schoolData)
        val gradRate = getGradRate(context, schoolData)
        val academics = getFullAcademicString(context, schoolData)
        val eligible = schoolData?.eligibility1 ?: ""
        val overview = schoolData?.overview_paragraph ?: ""
        val activities = getActivities(schoolData)
        val sports = getSports(schoolData)
        uiState.value = SchoolName(schoolName)
        uiState.value =
            if (eligible.isBlank()) HideEligibility else SchoolEligibility(eligible)
        uiState.value =
            if (contactAddress.isBlank()) HideAddress else SchoolAddress(getFormattedAddress())
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
            if (contactEmail.isBlank()) HideEmail else SchoolEmail(getFormattedEmail())
        uiState.value =
            if (contactPhone.isBlank()) HidePhone else SchoolPhone(getFormattedPhone())
    }

    private fun getFormattedAddress(): SpannableStringBuilder {
        if (contactAddress.isBlank()) return SpannableStringBuilder(contactAddress)
        return SpannableStringBuilder(contactAddress).apply {
            setSpan(
                clickableSpanAddress,
                0,
                contactAddress.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun getFormattedEmail(): SpannableStringBuilder {
        if (contactEmail.isBlank()) return SpannableStringBuilder(contactEmail)
        return SpannableStringBuilder(contactEmail).apply {
            setSpan(
                clickableSpanEmail,
                0,
                contactEmail.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun getFormattedPhone(): SpannableStringBuilder {
        if (contactPhone.isBlank()) return SpannableStringBuilder(contactPhone)
        return SpannableStringBuilder(contactPhone).apply {
            setSpan(
                clickableSpanPhone,
                0,
                contactPhone.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
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