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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NycSchoolsInfoRepository @Inject constructor(
    private val schoolsRemoteDataSource: NycSchoolsRemoteDataSource,
    private val schoolsDao: NycSchoolDataDao
) {
    suspend fun fetchAllSchoolSats(): Flow<DataFetchResult<List<NycSchoolSatData>>?> {
        return flow {
            emit(schoolsDao.fetchSatsCached())
            emit(DataFetchResult.loading())
            emit(schoolsRemoteDataSource.fetchSats()?.apply { schoolsDao.refreshSats(data) })
        }.flowOn(io())
    }

    suspend fun refreshSchools(): Flow<DataFetchResult<List<NycSchoolData>>?> {
        return flow {
            emit(DataFetchResult.loading())
            val schools = schoolsRemoteDataSource
                    .fetchSchools()
                    ?.apply { data?.forEach { schoolsDao.refreshSchool(it) } }
                    ?.data
            emit(DataFetchResult.success(schools))
        }.flowOn(io())
    }

    suspend fun fetchSchoolData(dbn: String?): Flow<DataFetchResult<NycSchoolData>?> {
        if (dbn.isNullOrBlank()) return flow { emit(DataFetchResult.error(message = "")) }
        return flow {
            emit(schoolsDao.fetchSchoolCached(dbn))
            emit(DataFetchResult.loading())
            emit(schoolsRemoteDataSource.fetchSchool(dbn)?.apply { schoolsDao.refreshSchool(data) })
        }.flowOn(io())
    }
}