package com.education.nycschools.schoolinfo.ui.main

sealed class NycSchoolInfoUiStates {
    object Loading: NycSchoolInfoUiStates()
    object NoData: NycSchoolInfoUiStates()
    object LoadSatList: NycSchoolInfoUiStates()
    class LoadSchoolDetail(val dbn: String): NycSchoolInfoUiStates()
}