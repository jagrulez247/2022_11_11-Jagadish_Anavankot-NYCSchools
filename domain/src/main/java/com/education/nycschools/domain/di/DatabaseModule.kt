package com.education.nycschools.domain.di

import android.content.Context
import androidx.room.Room
import com.education.nycschools.domain.BuildConfig
import com.education.nycschools.domain.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideDashboardDao(db: AppDatabase) = db.nycSchoolDataDao()
}