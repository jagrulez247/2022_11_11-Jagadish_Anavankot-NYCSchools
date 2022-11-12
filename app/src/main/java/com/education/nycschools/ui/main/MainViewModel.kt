package com.education.nycschools.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.nycschools.domain.data.NycSchoolsRepository
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.domain.network.NetworkDispatcher.main
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NycSchoolsRepository
) : ViewModel() {

    fun onViewCreated() {
        viewModelScope.launch(main()) {
            repository.fetchAllSchoolSats()
                .onStart { emit(DataFetchResult.loading()) }
                .collect {
                    val data = it?.data
                    val error = it?.error
                    Log.d("MainViewModel", data.toString())
                }
        }
    }
}