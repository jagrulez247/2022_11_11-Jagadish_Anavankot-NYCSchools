package com.education.nycschools.domain.data

import com.education.nycschools.domain.data.local.NycSchoolDataDao
import com.education.nycschools.domain.data.mappers.NycSchoolDataMapper
import com.education.nycschools.domain.data.remote.NycSchoolsRemoteDataSource
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.domain.models.NycSchoolData
import com.education.nycschools.domain.models.NycSchoolSatData
import com.education.nycschools.domain.network.NetworkDispatcher.io
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NycSchoolsRepository @Inject constructor(
    private val schoolsRemoteDataSource: NycSchoolsRemoteDataSource,
    private val schoolsDao: NycSchoolDataDao,
    private val schoolMapper: NycSchoolDataMapper
) {

    suspend fun fetchAllSchoolSats(): Flow<DataFetchResult<List<NycSchoolSatData>>?> {
        return flow {
            emit(fetchSchoolSatsCached())
            emit(DataFetchResult.loading())
            val mappedItems = schoolsRemoteDataSource
                .fetchAllSchoolSats()
                .takeIf { it.status == DataFetchResult.Status.SUCCESS }
                ?.data
                ?.map { schoolMapper.mapToNycSchoolSatData(it) }
                ?.let { DataFetchResult.success(it) }
            mappedItems?.data?.let {
                schoolsDao.deleteAllSats(it)
                schoolsDao.insertAllSats(it)
            }
            emit(mappedItems)
        }.flowOn(io())
    }

    suspend fun fetchSchoolData(dbn: String?): Flow<DataFetchResult<NycSchoolData>?> {
        if (dbn.isNullOrBlank()) return flow { emit(DataFetchResult.error(message = "")) }
        return flow {
            emit(fetchSchoolDataCached(dbn))
            emit(DataFetchResult.loading())
            emit(
                schoolsRemoteDataSource
                    .fetchSchoolData(dbn)
                    .takeIf { it.status == DataFetchResult.Status.SUCCESS }
                    ?.data
                    ?.let { schoolMapper.mapToNycSchoolData(it) }
                    ?.let { DataFetchResult.success(it) }
            )
        }.flowOn(io())
    }

    private fun fetchSchoolSatsCached(): DataFetchResult<List<NycSchoolSatData>>? =
        schoolsDao.getAllSats()?.let { DataFetchResult.success(it) }

    private fun fetchSchoolDataCached(dbn: String): DataFetchResult<NycSchoolData>? =
        schoolsDao.getSchoolData(dbn)?.takeIf { it.isNotEmpty() }
            ?.let { DataFetchResult.success(it[0]) }
}