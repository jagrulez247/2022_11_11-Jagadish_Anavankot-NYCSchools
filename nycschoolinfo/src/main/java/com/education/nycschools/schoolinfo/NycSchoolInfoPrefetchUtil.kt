package com.education.nycschools.schoolinfo

import com.education.nycschools.common.utils.LocalCoroutineDispatcher.main
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NycSchoolInfoPrefetchUtil @Inject constructor(private val repo: NycSchoolsInfoRepository) {

    private val prefetchScope by lazy { CoroutineScope(SupervisorJob() + main()) }

    fun fetchAndSaveAllSchoolSats(callback: (Boolean) -> Unit = {}) = prefetchScope.launch {
        repo.fetchAllSchoolSats().collect { callback(it?.data?.isNotEmpty() == true) }
    }

    fun refreshAllSchools(callback: (Boolean) -> Unit = {}) = prefetchScope.launch {
        repo.refreshSchools().collect { callback(it?.data?.isNotEmpty() == true) }
    }
}