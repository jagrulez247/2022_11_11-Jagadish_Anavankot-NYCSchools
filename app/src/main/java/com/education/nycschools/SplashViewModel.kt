package com.education.nycschools

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.nycschools.common.utils.LocalCoroutineDispatcher.main
import com.education.nycschools.schoolinfo.NycSchoolInfoPrefetchUtil
import com.education.nycschools.uicomponents.feature.data.NycSchoolInfoScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val prefetchUtil: NycSchoolInfoPrefetchUtil
) : ViewModel() {

    companion object {
        private const val API_LOAD_TIMEOUT = 1500L
        private const val TAG = "SplashViewModel"
    }

    private val uiState: MutableLiveData<SplashUiStates> = MutableLiveData()
    internal fun uiState(): LiveData<SplashUiStates> = uiState

    @Volatile private var isFinished: Boolean = false

    fun onCreate() {
        viewModelScope.launch(main()) {
            Log.d(TAG, "Initializing...")
            prefetchUtil.fetchAndSaveAllSchoolSats { loaded -> if (loaded) moveToMainNavScreens() }
            Log.d(TAG, "Fetch all school sats step done")
            prefetchUtil.refreshAllSchools()
            Log.d(TAG, "Fetch all schools step done")
            delay(API_LOAD_TIMEOUT) // Max time to wait before load is complete
            moveToMainNavScreens()
        }
    }

    @MainThread
    private fun moveToMainNavScreens() {
        if (isFinished) return
        isFinished = true
        uiState.value = SplashUiStates.MoveToAppScreens(NycSchoolInfoScreen())
    }
}