package com.education.nycschools.schoolinfo.ui.sats

import com.education.nycschools.domain.models.NycSchoolSatData

sealed class NycSchoolSatsUiStates {
    class UpdateSchoolSats(val sats: List<NycSchoolSatData>): NycSchoolSatsUiStates()
}