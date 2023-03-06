package com.mbrainz.sample.unit.ui.feature.search

import app.cash.turbine.test
import com.mbrainz.sample.TestFixtures
import com.mbrainz.sample.data.ArtistRepository
import com.mbrainz.sample.ui.feature.search.ArtistSearchState
import com.mbrainz.sample.ui.feature.search.ArtistSearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistSearchViewModelTest {
    private val mockRepository = mockk<ArtistRepository>()
    private lateinit var viewModel: ArtistSearchViewModel

    @Before
    fun setUp() {
        // GIVEN
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ArtistSearchViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN nominal case WHEN search for Artist with complete name THEN emit loading and results`() = runTest {
        // GIVEN
        coEvery { mockRepository.searchArtist(any()) } coAnswers {
            delay(200)
            listOf(TestFixtures.basicBandArtist)
        }

        // WHEN
        viewModel.searchArtist("sample")

        // THEN
        viewModel.searchedArtist.test {
            assertThat(awaitItem() is ArtistSearchState.Loading).isTrue
            val successState = awaitItem() // next item is result
            assertThat(successState is ArtistSearchState.Success).isTrue
            val result = (successState as ArtistSearchState.Success).result
            assertThat(result.size).isEqualTo(1)
            assertThat(result[0].id).isEqualTo(TestFixtures.artistId1)
        }
    }

    @Test
    fun `GIVEN nominal case WHEN search for Artist with 2 chars THEN emits empty results`() = runTest {
        // GIVEN
        coEvery { mockRepository.searchArtist(any()) } returns listOf(TestFixtures.basicBandArtist)

        // WHEN
        viewModel.searchArtist("sa")

        // THEN
        viewModel.searchedArtist.test {
            val successState = awaitItem()
            assertThat(successState is ArtistSearchState.Success).isTrue
            assertThat((successState as ArtistSearchState.Success).result.size).isEqualTo(0)
        }
    }

    @Test
    fun `GIVEN error case WHEN search for Artist with complete name THEN emit loading and error`() = runTest {
        // GIVEN
        coEvery { mockRepository.searchArtist(any()) } coAnswers {
            delay(200)
            throw Exception("oh noes")
        }

        // WHEN
        viewModel.searchArtist("sample")

        // THEN
        viewModel.searchedArtist.test {
            assertThat(awaitItem() is ArtistSearchState.Loading).isTrue
            assertThat(awaitItem() is ArtistSearchState.Error).isTrue
        }
    }
}
