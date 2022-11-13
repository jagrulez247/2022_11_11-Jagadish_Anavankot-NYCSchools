package com.education.nycschools.schoolinfo.ui.detail

sealed class NycSchoolDetailUiStates {
    object Loading: NycSchoolDetailUiStates()
    object Loaded: NycSchoolDetailUiStates()
    object NoData: NycSchoolDetailUiStates()
    class SchoolName(val name: String): NycSchoolDetailUiStates()
    class SchoolTotalStudents(val total: String): NycSchoolDetailUiStates()
    class SchoolGradRate(val rate: String): NycSchoolDetailUiStates()
    class SchoolDescription(val overview: String): NycSchoolDetailUiStates()
}