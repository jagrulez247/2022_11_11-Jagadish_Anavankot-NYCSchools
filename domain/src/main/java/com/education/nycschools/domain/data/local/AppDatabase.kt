package com.education.nycschools.domain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.education.nycschools.domain.BuildConfig
import com.education.nycschools.domain.models.NycSchoolData
import com.education.nycschools.domain.models.NycSchoolSatData

@Database(
    entities = [NycSchoolData::class, NycSchoolSatData::class],
    version = BuildConfig.DATABASE_VERSION,
    exportSchema = BuildConfig.DATABASE_EXPORT_SCHEMA
)
@TypeConverters(NycSchoolDataConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun nycSchoolDataDao(): NycSchoolDataDao
}