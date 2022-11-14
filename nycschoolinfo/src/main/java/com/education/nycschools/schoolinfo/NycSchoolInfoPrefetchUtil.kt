package com.education.nycschools.schoolinfo

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
        satFetchJob = prefetchScope.launch(main()) {
            repo.fetchAllSchoolSats().collect { cancelOnSuccess(it, satFetchJob) }
        }
        schoolFetchJob = prefetchScope.launch(main()) {
            repo.refreshSchools().collect { cancelOnSuccess(it, schoolFetchJob) }
        }
    }

    private fun <T> cancelOnSuccess(result: DataFetchResult<List<T>>?, job: Job?) {
        val isSuccess = result?.isSuccessStatus() ?: false && result?.data?.isNotEmpty() == true
        if (isSuccess) job?.cancel()
    }
}