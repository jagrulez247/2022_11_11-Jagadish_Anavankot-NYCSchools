package com.education.nycschools.schoolinfo.ui.detail

import androidx.lifecycle.ViewModel
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NycSchoolDetailViewModel @Inject constructor(
    private val repository: NycSchoolsInfoRepository
): ViewModel() {
}