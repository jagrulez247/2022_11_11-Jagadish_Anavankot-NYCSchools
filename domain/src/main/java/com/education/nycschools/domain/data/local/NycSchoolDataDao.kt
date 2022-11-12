package com.education.nycschools.domain.data.local

import androidx.room.*
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
}