package com.education.nycschools.schoolinfo

import android.util.Log
import com.education.nycschools.common.utils.LocalCoroutineDispatcher.main
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import com.education.nycschools.domain.models.DataFetchResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NycSchoolInfoPrefetchUtil @Inject constructor(private val repo: NycSchoolsInfoRepository) {

    private val prefetchScope by lazy { CoroutineScope(SupervisorJob() + main()) }
    private var satFetchJob: Job? = null
    private var schoolFetchJob: Job? = null

    fun refresh() {
        satFetchJob = prefetchScope.launch(main()) { refreshSchoolSats() }
        schoolFetchJob = prefetchScope.launch(main()) { refreshSchools() }
    }

    private suspend fun refreshSchoolSats() {
        repo
            .fetchAllSchoolSats()
            .collect { processResult(it, satFetchJob, "satList") }
    }

    private suspend fun refreshSchools() {
        repo
            .refreshSchools()
            .collect { processResult(it, schoolFetchJob, "schoolList") }
    }

    private fun <T> processResult(result: DataFetchResult<List<T>>?, job: Job?, tag: String) {
        val isLoading = result?.isLoadingStatus() ?: false
        val isSuccess = result?.isSuccessStatus() ?: false && result?.data?.isNotEmpty() == true
        when {
            isLoading -> Log.d("PrefetchUtil", "Loading $tag results")
            isSuccess -> {
                job?.cancel()
                Log.d(
                    "PrefetchUtil",
                    "Fetch complete, $tag of size: ${result?.data?.size ?: 0}"
                )
            }
        }
    }
}