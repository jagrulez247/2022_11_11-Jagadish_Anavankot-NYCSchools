package com.education.nycschools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.nycschools.common.utils.LocalCoroutineDispatcher.main
import com.education.nycschools.schoolinfo.NycSchoolInfoPrefetchUtil
import com.education.nycschools.uicomponents.feature.data.NycSchoolInfoScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    prefetchUtil: NycSchoolInfoPrefetchUtil
) : ViewModel() {

    private val uiState: MutableLiveData<SplashUiStates> = MutableLiveData()
    internal fun uiState(): LiveData<SplashUiStates> = uiState

    init {
        prefetchUtil.refresh()
    }

    fun onCreate() {
        viewModelScope.launch(main()) {
            val destination = NycSchoolInfoScreen().setEnterAnimation(
                com.education.nycschools.uicomponents.R.anim.anim_slide_right_to_left
            )
            uiState.value = SplashUiStates.MoveToAppScreens(destination)
            uiState.value = SplashUiStates.Finish
        }
    }
}