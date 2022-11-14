package com.education.nycschools.uicomponents.feature

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainNavSharedViewModel @Inject constructor(): ViewModel() {

    private val satItemSelected = MutableLiveData<String>()

    fun listenForSatItemSelection(): LiveData<String> = satItemSelected

    @MainThread
    fun satListItemSelected(dbn: String) {
        satItemSelected.value = dbn
    }
}