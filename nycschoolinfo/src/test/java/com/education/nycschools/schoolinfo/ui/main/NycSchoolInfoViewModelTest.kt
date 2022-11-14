package com.education.nycschools.schoolinfo.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.domain.models.NycSchoolSatData
import com.education.nycschools.schoolinfo.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NycSchoolInfoViewModelTest {

    companion object {
        private val MOCK_SAT_LIST = mutableListOf<NycSchoolSatData>().apply {
            add(NycSchoolSatData(dbn = "adcdef", "School 1"))
            add(NycSchoolSatData(dbn = "qwerty", "School 2"))
            add(NycSchoolSatData(dbn = "zxcvbn", "School 3"))
        }
    }

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var repository: NycSchoolsInfoRepository

    @RelaxedMockK
    private lateinit var stateObserver: Observer<NycSchoolInfoUiStates>

    private lateinit var viewModel: NycSchoolInfoViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = NycSchoolInfoViewModel(repository)
        viewModel.uiState().observeForever(stateObserver)
    }

    @After
    fun tearDown() {
        if (::stateObserver.isInitialized) viewModel.uiState().removeObserver(stateObserver)
    }

    @Test
    fun shouldHandleLoadingScenario() {
        givenSatListFetchLoading()
        whenOnViewCreated()
        thenLoadingStateIsSent()
        thenNoDataIsNotSent()
        thenLoadedStateIsNotSent()
    }

    @Test
    fun shouldHandleErrorScenario() {
        givenSatListFetchError()
        whenOnViewCreated()
        thenNoDataIsSent()
        thenLoadingStateIsNotSent()
        thenLoadedStateIsNotSent()
    }

    @Test
    fun shouldHandleSuccessScenario() {
        givenSatListFetchSuccess()
        whenOnViewCreated()
        thenLoadedStateIsSent()
        thenNoDataIsNotSent()
        thenLoadingStateIsNotSent()
    }

    private fun givenSatListFetchSuccess() {
        coEvery {
            repository.fetchAllSchoolSats()
        } returns flowOf(DataFetchResult.success(MOCK_SAT_LIST))
    }

    private fun givenSatListFetchError() {
        coEvery {
            repository.fetchAllSchoolSats()
        } returns flowOf(DataFetchResult.error("Error message"))
    }

    private fun givenSatListFetchLoading() {
        coEvery {
            repository.fetchAllSchoolSats()
        } returns flowOf(DataFetchResult.loading())
    }

    private fun whenOnViewCreated() {
        viewModel.onViewCreated()
    }

    private fun thenLoadingStateIsSent() {
        verify { stateObserver.onChanged(NycSchoolInfoUiStates.Loading) }
    }

    private fun thenLoadedStateIsSent() {
        verify { stateObserver.onChanged(NycSchoolInfoUiStates.Loaded) }
    }

    private fun thenNoDataIsSent() {
        verify { stateObserver.onChanged(NycSchoolInfoUiStates.NoData) }
    }

    private fun thenNoDataIsNotSent() {
        verify(exactly = 0) { stateObserver.onChanged(NycSchoolInfoUiStates.NoData) }
    }

    private fun thenLoadedStateIsNotSent() {
        verify(exactly = 0) { stateObserver.onChanged(NycSchoolInfoUiStates.Loaded) }
    }

    private fun thenLoadingStateIsNotSent() {
        verify(exactly = 0) { stateObserver.onChanged(NycSchoolInfoUiStates.Loading) }
    }
}