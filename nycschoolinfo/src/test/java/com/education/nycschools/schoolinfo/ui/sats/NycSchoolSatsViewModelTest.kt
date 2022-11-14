package com.education.nycschools.schoolinfo.ui.sats

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.education.nycschools.domain.data.NycSchoolsInfoRepository
import com.education.nycschools.domain.models.DataFetchResult
import com.education.nycschools.domain.models.NycSchoolSatData
import com.education.nycschools.schoolinfo.CoroutineTestRule
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NycSchoolSatsViewModelTest {

    companion object {
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

        private const val INVALID_SEARCH_QUERY = "lkjhg"
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
    private lateinit var stateObserver: Observer<NycSchoolSatsUiStates>

    private lateinit var viewModel: NycSchoolSatsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { repository.getSatsFromCache() } returns listOf()
        viewModel = NycSchoolSatsViewModel(repository)
        viewModel.uiState().observeForever(stateObserver)
    }

    @After
    fun tearDown() {
        if (::stateObserver.isInitialized) viewModel.uiState().removeObserver(stateObserver)
    }

    @Test
    fun shouldShowNoDataIfEmptyData() {
        givenNoData()
        whenOnViewCreated()
        thenShowNoData()
    }

    @Test
    fun shouldNotCollectIfIsInMemory() {
        givenInMemoryCache()
        whenOnViewCreated()
        thenDoNotCollectFromApi()
    }

    @Test
    fun shouldFetchFromApiIfCacheIsEmpty() {
        givenEmptyCache()
        givenApiSatList()
        whenOnViewCreated()
        thenCollectFromApi()
    }

    @Test
    fun shouldSelectFirstItemByDefaultOnStart() {
        givenInMemoryCache()
        whenOnViewCreated()
        thenOnlyFirstItemIsSelected()
    }

    @Test
    fun shouldUpdateUiProperlyOnStart() {
        givenInMemoryCache()
        whenOnViewCreated()
        thenUpdateList(MOCK_CACHED_SAT_LIST)
        thenInformSelection(MOCK_CACHED_SAT_LIST)
        thenDismissKeyboard()
    }

    @Test
    fun shouldReturnOriginalDataOnEmptySearchQuery() {
        givenInMemoryCache()
        whenOnViewCreated()
        whenOnSearchQuery("")
        thenUpdateList(MOCK_CACHED_SAT_LIST)
    }

    @Test
    fun shouldShowNoDataOnInvalidSearchQuery() {
        givenInMemoryCache()
        whenOnViewCreated()
        whenOnSearchQuery(INVALID_SEARCH_QUERY)
        thenShowNoData()
    }

    @Test
    fun shouldShowProperResultsOnSearch() {
        givenInMemoryCache()
        whenOnViewCreated()
        whenOnSearchQuery(MOCK_CACHED_SAT_LIST[0].school_name)
        thenUpdateList(listOf(MOCK_CACHED_SAT_LIST[0]))
    }

    private fun givenNoData() {
        every { repository.getSatsFromCache() } returns listOf()
        coEvery {
            repository.fetchAllSchoolSats()
        } returns flowOf(DataFetchResult.error(""))
    }

    private fun givenEmptyCache() {
        every { repository.getSatsFromCache() } returns listOf()
    }

    private fun givenInMemoryCache() {
        every { repository.getSatsFromCache() } returns MOCK_CACHED_SAT_LIST
    }

    private fun givenApiSatList() {
        coEvery {
            repository.fetchAllSchoolSats()
        } returns flowOf(DataFetchResult.success(MOCK_API_SAT_LIST))
    }

    private fun whenOnViewCreated() {
        viewModel.onViewCreated()
    }

    private fun whenOnSearchQuery(query: String?) {
        viewModel.onSearchQuery(query)
    }

    private fun thenDoNotCollectFromApi() {
        coVerify (exactly = 0) { repository.fetchAllSchoolSats() }
    }

    private fun thenCollectFromApi() {
        coVerify { repository.fetchAllSchoolSats() }
    }

    private fun thenOnlyFirstItemIsSelected() {
        assertTrue(viewModel.schoolSats.count { it.selected == true } == 1)
        assertTrue(viewModel.schoolSats.indexOfFirst { it.selected == true } == 0)
    }

    private fun thenDismissKeyboard() {
        verify(exactly = 1) { stateObserver.onChanged(NycSchoolSatsUiStates.DismissKeyboard) }
    }

    private fun thenShowNoData() {
        verify { stateObserver.onChanged(NycSchoolSatsUiStates.ShowNoData) }
    }

    private fun thenUpdateList(expectedList: List<NycSchoolSatData>) {
        verify { stateObserver.onChanged(NycSchoolSatsUiStates.HideNoData) }
        verify(exactly = 0) { stateObserver.onChanged(NycSchoolSatsUiStates.ShowNoData) }
        val slots = mutableListOf<NycSchoolSatsUiStates>()
        verify { stateObserver.onChanged(capture(slots)) }

        val updateListState = slots
            .findLast { it is NycSchoolSatsUiStates.UpdateSchoolSats }
            ?.let { it as NycSchoolSatsUiStates.UpdateSchoolSats }
        val actualList = updateListState?.sats ?: mutableListOf()
        assertTrue(actualList.size == expectedList.size)
        actualList.forEachIndexed { index, satData ->
            assertEquals(expectedList[index].dbn, satData.dbn)
        }
    }

    private fun thenInformSelection(expectedList: List<NycSchoolSatData>) {
        val slots = mutableListOf<NycSchoolSatsUiStates>()
        verify { stateObserver.onChanged(capture(slots)) }

        val informSelectionState = slots
            .find { it is NycSchoolSatsUiStates.InformItemSelection }
            ?.let { it as NycSchoolSatsUiStates.InformItemSelection }
        assertEquals(expectedList[0].dbn, informSelectionState?.dbn)
    }
}