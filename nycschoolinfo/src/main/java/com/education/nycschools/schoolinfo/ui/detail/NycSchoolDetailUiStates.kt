package com.education.nycschools.schoolinfo.ui.detail

import android.text.SpannableStringBuilder

sealed class NycSchoolDetailUiStates {
    object Loading: NycSchoolDetailUiStates()
    object Loaded: NycSchoolDetailUiStates()
    object NoData: NycSchoolDetailUiStates()
    object HideEligibility: NycSchoolDetailUiStates()
    object HideAddress: NycSchoolDetailUiStates()
    object HideEmail: NycSchoolDetailUiStates()
    object HidePhone: NycSchoolDetailUiStates()
    object HideAcademics: NycSchoolDetailUiStates()
    object HideActivities: NycSchoolDetailUiStates()
    object HideSports: NycSchoolDetailUiStates()
    class SchoolName(val name: String): NycSchoolDetailUiStates()
    class SchoolEligibility(val eligibility: String): NycSchoolDetailUiStates()
    class SchoolAddress(val address: String): NycSchoolDetailUiStates()
    class SchoolEmail(val email: SpannableStringBuilder): NycSchoolDetailUiStates()
    class SchoolPhone(val phone: SpannableStringBuilder): NycSchoolDetailUiStates()
    class SchoolTotalStudents(val total: String): NycSchoolDetailUiStates()
    class SchoolGradRate(val rate: String): NycSchoolDetailUiStates()
    class SchoolDescription(val overview: String): NycSchoolDetailUiStates()
    class SchoolAcademics(val academics: String): NycSchoolDetailUiStates()
    class SchoolActivities(val activities: String): NycSchoolDetailUiStates()
    class SchoolSports(val sports: String): NycSchoolDetailUiStates()
}