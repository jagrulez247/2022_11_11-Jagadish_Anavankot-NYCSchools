package com.education.nycschools.schoolinfo.ui.main

sealed class NycSchoolInfoUiStates {
    object Loading: NycSchoolInfoUiStates()
    object Loaded: NycSchoolInfoUiStates()
    object NoData: NycSchoolInfoUiStates()
    class LoadError(val message: String): NycSchoolInfoUiStates()
    class LoadSchoolDetail(val dbn: String): NycSchoolInfoUiStates()
}