package com.education.nycschools.domain.data

import com.education.nycschools.common.utils.LocalCoroutineDispatcher.io
import com.education.nycschools.domain.data.local.NycSchoolDataDao
import com.education.nycschools.domain.data.remote.NycSchoolsRemoteDataSource
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.domain.models.NycSchoolData
import com.education.nycschools.domain.models.NycSchoolSatData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NycSchoolsInfoRepository @Inject constructor(
    private val schoolsRemoteDataSource: NycSchoolsRemoteDataSource,
    private val schoolsDao: NycSchoolDataDao
) {
    private val schoolSats: CopyOnWriteArrayList<NycSchoolSatData> = CopyOnWriteArrayList()
    private val schoolData: ConcurrentHashMap<String, NycSchoolData> = ConcurrentHashMap()

    fun getSatsFromCache(): List<NycSchoolSatData> = schoolSats

    fun getSchoolDetailFromCache(dbn: String): NycSchoolData? = schoolData[dbn]

    suspend fun fetchAllSchoolSats(): Flow<DataFetchResult<List<NycSchoolSatData>>?> {
        return flow {
            emit(schoolsDao.fetchSatsCached(backup = schoolSats))
            emit(DataFetchResult.loading())
            emit(schoolsRemoteDataSource.fetchSats()
                .apply {
                    if (!data.isNullOrEmpty()) {
                        schoolsDao.refreshSats(data)
                        schoolSats.clear()
                        schoolSats.addAll(data)
                    }
                }
            )
        }.flowOn(io())
    }

    suspend fun refreshSchools(): Flow<DataFetchResult<List<NycSchoolData>>?> {
        return flow {
            emit(schoolsDao.fetchSchoolsCached(backup = schoolData))
            emit(DataFetchResult.loading())
            val schools = schoolsRemoteDataSource
                .fetchSchools()
                .apply {
                    if (!data.isNullOrEmpty()) {
                        schoolData.clear()
                        data.forEach {
                            schoolsDao.refreshSchool(it)
                            schoolData[it.dbn] = it
                        }
                    }
                }.data
            emit(DataFetchResult.success(schools))
        }.flowOn(io())
    }

    suspend fun fetchSchoolData(dbn: String?): Flow<DataFetchResult<NycSchoolData>?> {
        if (dbn.isNullOrBlank()) return flow { emit(DataFetchResult.error(message = "")) }
        return flow {
            emit(schoolsDao.fetchSchoolCached(dbn))
            emit(DataFetchResult.loading())
            emit(schoolsRemoteDataSource.fetchSchool(dbn).apply { schoolsDao.refreshSchool(data) })
        }.flowOn(io())
    }
}