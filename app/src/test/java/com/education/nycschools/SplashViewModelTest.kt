package com.education.nycschools

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.education.nycschools.schoolinfo.NycSchoolInfoPrefetchUtil
import com.education.nycschools.uicomponents.feature.data.NycSchoolInfoScreen
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class SplashViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var prefetchUtil: NycSchoolInfoPrefetchUtil

    @RelaxedMockK
    private lateinit var stateObserver: Observer<SplashUiStates>

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SplashViewModel(prefetchUtil)
        viewModel.uiState().observeForever(stateObserver)
    }

    @After
    fun tearDown() {
        if (::stateObserver.isInitialized) viewModel.uiState().removeObserver(stateObserver)
    }

    @Test
    fun shouldPrefetchOnInit() {
        verify { prefetchUtil.refresh() }
    }

    @Test
    fun shouldMoveToAppScreenOnCreate() {
        whenOnCreate()
        thenMovesToAppScreen()
        thenCallsFinish()
    }

    private fun whenOnCreate() {
        viewModel.onCreate()
    }

    private fun thenMovesToAppScreen() {
        val slots = mutableListOf<SplashUiStates>()
        verify { stateObserver.onChanged(capture(slots)) }
        val destinationState = slots
            .find { it is SplashUiStates.MoveToAppScreens }
            ?.let { it as SplashUiStates.MoveToAppScreens }
        val fragmentData = destinationState?.data
        assertTrue(fragmentData is NycSchoolInfoScreen)
    }

    private fun thenCallsFinish() {
        verify { stateObserver.onChanged(SplashUiStates.Finish) }
    }
}