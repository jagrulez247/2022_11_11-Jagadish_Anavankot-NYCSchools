package com.education.nycschools.domain.data.local

import androidx.room.*
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.domain.models.NycSchoolData
import com.education.nycschools.domain.models.NycSchoolSatData

@Dao
interface NycSchoolDataDao {

    @Query("SELECT * FROM nycschoolsats order by school_name DESC")
    fun getAllSats(): List<NycSchoolSatData>?

    @Query("SELECT * FROM nycschools where dbn = :dbn")
    fun getSchoolData(dbn: String): List<NycSchoolData>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSats(satData: List<NycSchoolSatData>)

    @Delete
    fun deleteAllSats(satData: List<NycSchoolSatData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchoolData(data: NycSchoolData)

    fun fetchSatsCached(): DataFetchResult<List<NycSchoolSatData>>? {
        return getAllSats()?.let { DataFetchResult.success(it) }
    }

    fun fetchSchoolCached(dbn: String): DataFetchResult<NycSchoolData>? {
        return getSchoolData(dbn)?.takeIf { it.isNotEmpty() }
            ?.let { DataFetchResult.success(it[0]) }
    }

    fun refreshSats(allSats: List<NycSchoolSatData>?) {
        if (allSats.isNullOrEmpty()) return
        deleteAllSats(allSats)
        insertAllSats(allSats)
    }

    fun refreshSchool(data: NycSchoolData?) {
        if (data == null) return
        insertSchoolData(data)
    }
}