package com.education.nycschools.domain.data.remote

import com.education.nycschools.domain.models.NycSchoolDataItem
import com.education.nycschools.domain.models.NycSchoolSatsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NycSchoolsApiService {
    @GET("s3k6-pzi2.json")
    suspend fun getSchoolData(@Query("dbn") dbn: String?): Response<List<NycSchoolDataItem>>

    @GET("s3k6-pzi2.json")
    suspend fun getAllSchools(): Response<List<NycSchoolDataItem>>

    @GET("f9bf-2cp4.json")
    suspend fun getAllSchoolSats(): Response<List<NycSchoolSatsItem>>


}