package com.education.nycschools.domain.data.remote

import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.domain.models.NycSchoolDataItem
import com.education.nycschools.domain.models.NycSchoolSatsItem
import com.education.nycschools.domain.network.NetworkUtils.getResponse
import retrofit2.Retrofit
import javax.inject.Inject

class NycSchoolsRemoteDataSource @Inject constructor(
    private val retrofit: Retrofit,
    private val apiService: NycSchoolsApiService
) {

    suspend fun fetchSchoolData(dbn: String?): DataFetchResult<NycSchoolDataItem> {
        return retrofit.getResponse(
            request = { apiService.getSchoolData(dbn) },
            defaultErrorMessage = "Error fetching school basic data"
        )
    }

    suspend fun fetchAllSchoolSats(): DataFetchResult<List<NycSchoolSatsItem>> {
        return retrofit.getResponse(
            request = { apiService.getAllSchoolSats() },
            defaultErrorMessage = "Error fetching school sat data"
        )
    }
}