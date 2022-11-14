package com.education.nycschools.schoolinfo.ui.sats

import com.education.nycschools.domain.models.NycSchoolSatData

sealed class NycSchoolSatsUiStates {
    object ShowNoData: NycSchoolSatsUiStates()
    object HideNoData: NycSchoolSatsUiStates()
    class UpdateSchoolSats(val sats: List<NycSchoolSatData>): NycSchoolSatsUiStates()
    class InformItemSelection(val dbn: String): NycSchoolSatsUiStates()
    object DismissKeyboard: NycSchoolSatsUiStates()
}