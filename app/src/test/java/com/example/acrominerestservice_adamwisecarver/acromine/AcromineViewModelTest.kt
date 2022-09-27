package com.example.acrominerestservice_adamwisecarver.acromine

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.acrominerestservice_adamwisecarver.data.api.AcromineRepository
import com.example.acrominerestservice_adamwisecarver.data.model.AbbreviationItem
import com.example.acrominerestservice_adamwisecarver.data.model.AbbreviationList
import com.example.acrominerestservice_adamwisecarver.utils.ApiState
import com.example.acrominerestservice_adamwisecarver.viewmodel.AcromineViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AcromineViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockRepository = mockk<AcromineRepository>(relaxed = true)
    private lateinit var mockViewModel: AcromineViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockViewModel = AcromineViewModel(mockRepository, testDispatcher)
    }

    @Test
    fun `ApiState is loading`() {
        every { mockRepository.fetchAbbreviation("hm") } returns flowOf(ApiState.Loading)
        val responseHelper = mutableListOf<ApiState>()
        mockViewModel.stateLiveData.observeForever { responseHelper.add(it) }

        mockViewModel.fetchAbbreviation("hm")
        assertThat(responseHelper[0]).isInstanceOf(ApiState.Loading::class.java)
    }

    @Test
    fun `ApiState gets an error`() {
        every { mockRepository.fetchAbbreviation("hm") } returns flowOf(
            ApiState.Error(
                getMockErrorState()
            )
        )
        val responseHelper = mutableListOf<ApiState>()
        mockViewModel.stateLiveData.observeForever { responseHelper.add(it) }

        mockViewModel.fetchAbbreviation("hm")
        assertThat(responseHelper[0]).isInstanceOf(ApiState.Error::class.java)
    }

    @Test
    fun `ApiState loaded in successfully`() {
        every { mockRepository.fetchAbbreviation("hm") } returns flowOf(
            ApiState.Success(
                getMockSuccessState()
            )
        )
        val responseHelper = mutableListOf<ApiState>()
        mockViewModel.stateLiveData.observeForever { responseHelper.add(it) }

        mockViewModel.fetchAbbreviation("hm")
        assertThat(responseHelper[0]).isInstanceOf(ApiState.Success::class.java)
    }

    private fun getMockErrorState() = Exception("Could not find any matches")
    private fun getMockSuccessState() =
        AbbreviationList(
            sf = "HMM",
            lfs = listOf(
                AbbreviationItem(
                    lf = "hm",
                    freq = 1,
                    since = 1234,
                    vars = listOf(
                        AbbreviationItem(
                            lf = "HM",
                            freq = 2,
                            since = 5678,
                            vars = null
                        )
                    )
                ),
                AbbreviationItem(
                    lf = "df",
                    freq = 1,
                    since = 1234,
                    vars = listOf(
                        AbbreviationItem(
                            lf = "DF",
                            freq = 2,
                            since = 5678,
                            vars = null
                        )
                    )
                )
            )
        )

    @After
    fun shutdownTest() {
        clearAllMocks()
        Dispatchers.resetMain()
    }
}