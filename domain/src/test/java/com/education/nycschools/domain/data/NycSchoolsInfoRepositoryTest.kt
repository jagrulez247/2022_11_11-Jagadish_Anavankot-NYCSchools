package com.education.nycschools.domain.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.education.nycschools.domain.CoroutineTestRule
import com.education.nycschools.domain.data.local.NycSchoolDataDao
import com.education.nycschools.domain.data.remote.NycSchoolsRemoteDataSource
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.domain.models.NycSchoolData
import com.education.nycschools.domain.models.NycSchoolSatData
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NycSchoolsInfoRepositoryTest {

    companion object {
        private val MOCK_API_SCHOOL_LIST = mutableListOf<NycSchoolData>().apply {
            add(NycSchoolData(dbn = "adcdef", "School 1"))
            add(NycSchoolData(dbn = "qwerty", "School 2"))
            add(NycSchoolData(dbn = "zxcvbn", "School 3"))
        }

        private val MOCK_API_SAT_LIST = mutableListOf<NycSchoolSatData>().apply {
            add(NycSchoolSatData(dbn = "adcdef", "School 1"))
            add(NycSchoolSatData(dbn = "qwerty", "School 2"))
            add(NycSchoolSatData(dbn = "zxcvbn", "School 3"))
        }

        private val MOCK_CACHED_SAT_LIST = mutableListOf<NycSchoolSatData>().apply {
            add(NycSchoolSatData(dbn = "rtyuf", "School 4"))
            add(NycSchoolSatData(dbn = "dfghj", "School 5"))
            add(NycSchoolSatData(dbn = "mnbvc", "School 6"))
        }
    }

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var remoteDataSource: NycSchoolsRemoteDataSource

    @RelaxedMockK
    private lateinit var schoolsDao: NycSchoolDataDao

    private lateinit var repository: NycSchoolsInfoRepository
    private val schoolSats: MutableList<NycSchoolSatData> = mutableListOf()
    private val schoolData: MutableMap<String, NycSchoolData> = mutableMapOf()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = NycSchoolsInfoRepository(remoteDataSource, schoolsDao)
        every {
            schoolsDao.fetchSatsCached(mutableListOf())
        } returns DataFetchResult.success(mutableListOf())
        coEvery {
            remoteDataSource.fetchSats()
        } returns DataFetchResult.success(mutableListOf())
        every {
            schoolsDao.fetchSchoolsCached(mutableMapOf())
        } returns DataFetchResult.success(mutableListOf())
        coEvery {
            remoteDataSource.fetchSchools()
        } returns DataFetchResult.success(mutableListOf())
    }

    @Test
    fun shouldShowNoDataIfEmptyData() {
        givenSatListFromDb()
        whenFetchAllSchoolSats()
        thenCheckSatsAreCollected(MOCK_CACHED_SAT_LIST)
    }

    @Test
    fun shouldSaveSatDataToCacheWhenFromRemote() {
        givenSatListFromRemote()
        whenFetchAllSchoolSats()
        thenCheckSatsAreCollected(MOCK_API_SAT_LIST)
        thenSatResultsSavedToCache(MOCK_API_SAT_LIST)
    }

    @Test
    fun shouldSaveSchoolDataToCacheWhenFromRemote() {
        givenSchoolDataFromRemote()
        whenFetchAllSchoolData()
        thenCheckSchoolListDataIsCollected(MOCK_API_SCHOOL_LIST)
        thenSchoolDataIsSavedToCache(MOCK_API_SCHOOL_LIST)
    }

    private fun givenSatListFromDb() {
        every { schoolsDao.getAllSats() } returns MOCK_CACHED_SAT_LIST
        every {
            schoolsDao.fetchSatsCached(mutableListOf())
        } returns DataFetchResult.success(MOCK_CACHED_SAT_LIST)
    }

    private fun givenSatListFromRemote() {
        coEvery {
            remoteDataSource.fetchSats()
        } returns DataFetchResult.success(MOCK_API_SAT_LIST)
    }

    private fun givenSchoolDataFromRemote() {
        coEvery {
            remoteDataSource.fetchSchools()
        } returns DataFetchResult.success(MOCK_API_SCHOOL_LIST)
    }

    private fun whenFetchAllSchoolSats() {
        runTest { repository.fetchAllSchoolSats().collect {
            if (it?.data?.isNotEmpty() == true) {
                schoolSats.clear()
                schoolSats.addAll(it.data ?: mutableListOf())
            }
        } }
    }

    private fun whenFetchAllSchoolData() {
        runTest { repository.refreshSchools().collect { result ->
            if (result?.data?.isNotEmpty() == true) {
                schoolSats.clear()
                result.data?.forEach { schoolData[it.dbn] = it }
            }
        } }
    }

    private fun thenCheckSatsAreCollected(expectedList: List<NycSchoolSatData>) {
        assertEquals(expectedList.size, schoolSats.size)
        expectedList.forEachIndexed { index, nycSchoolSatData ->
            assertEquals(nycSchoolSatData.dbn, schoolSats[index].dbn)
        }
    }

    private fun thenSatResultsSavedToCache(expectedList: List<NycSchoolSatData>) {
        val actualList = repository.getSatsFromCache()
        assertEquals(expectedList.size, actualList.size)
        expectedList.forEachIndexed { index, nycSchoolSatData ->
            assertEquals(nycSchoolSatData.dbn, actualList[index].dbn)
        }
    }

    private fun thenCheckSchoolListDataIsCollected(expectedList: List<NycSchoolData>) {
        assertEquals(expectedList.size, schoolData.size)
        expectedList.forEachIndexed { index, nycSchoolData ->
            assertTrue(schoolData.containsKey(nycSchoolData.dbn))
        }
    }

    private fun thenSchoolDataIsSavedToCache(expectedList: List<NycSchoolData>) {
        expectedList.forEachIndexed { index, nycSchoolData ->
            assertEquals(nycSchoolData, repository.getSchoolDetailFromCache(nycSchoolData.dbn))
        }
    }
}