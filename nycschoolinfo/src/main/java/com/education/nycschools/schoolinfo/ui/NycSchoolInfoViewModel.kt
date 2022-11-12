package com.education.nycschools.schoolinfo.ui

import androidx.lifecycle.ViewModel
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NycSchoolInfoViewModel @Inject constructor(
    private val repository: NycSchoolsInfoRepository
): ViewModel() {
}