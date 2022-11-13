package com.education.nycschools.domain.data.remote

import com.education.nycschools.domain.data.mappers.NycSchoolDataMapper
import com.education.nycschools.domain.models.*
import com.education.nycschools.domain.network.NetworkUtils.getResponse
import retrofit2.Retrofit
import javax.inject.Inject

class NycSchoolsRemoteDataSource @Inject constructor(
    private val retrofit: Retrofit,
    private val apiService: NycSchoolsApiService,
    private val mapper: NycSchoolDataMapper
) {

    suspend fun fetchSchool(dbn: String?): DataFetchResult<NycSchoolData> {
        val apiResponse = retrofit.getResponse(
            request = { apiService.getSchoolData(dbn) },
            defaultErrorMessage = "Error fetching school basic data"
        )
        return when (apiResponse.status == DataFetchResult.Status.SUCCESS) {
            true -> apiResponse
                .data
                ?.takeIf { it.isNotEmpty() }
                ?.map { mapper.mapToNycSchoolData(it) }
                ?.let { DataFetchResult.success(it[0]) }
                ?: DataFetchResult.error("Unknown Error")
            else -> DataFetchResult.error(apiResponse.message ?: "", apiResponse.error)
        }
    }


    suspend fun fetchSchools(): DataFetchResult<List<NycSchoolData>> {
        val apiResponse = retrofit.getResponse(
            request = { apiService.getAllSchools() },
            defaultErrorMessage = "Error fetching all schools basic data"
        )
        return when (apiResponse.status == DataFetchResult.Status.SUCCESS) {
            true -> apiResponse
                .data
                ?.takeIf { it.isNotEmpty() }
                ?.map { mapper.mapToNycSchoolData(it) }
                ?.let { DataFetchResult.success(it) }
                ?: DataFetchResult.error("Unknown Error")
            else -> DataFetchResult.error(apiResponse.message ?: "", apiResponse.error)
        }
    }

    suspend fun fetchSats(): DataFetchResult<List<NycSchoolSatData>> {
        val apiResponse = retrofit.getResponse(
            request = { apiService.getAllSchoolSats() },
            defaultErrorMessage = "Error fetching all schools sat data"
        )
        return when (apiResponse.status == DataFetchResult.Status.SUCCESS) {
            true -> apiResponse
                .data
                ?.takeIf { it.isNotEmpty() }
                ?.map { mapper.mapToNycSchoolSatData(it) }
                ?.let { DataFetchResult.success(it) }
                ?: DataFetchResult.error("Unknown Error")
            else -> DataFetchResult.error(apiResponse.message ?: "", apiResponse.error)
        }
    }
}