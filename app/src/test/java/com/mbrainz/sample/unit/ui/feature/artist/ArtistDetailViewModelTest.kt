package com.mbrainz.sample.unit.ui.feature.artist

import app.cash.turbine.test
import com.mbrainz.sample.TestFixtures
import com.mbrainz.sample.data.ArtistRepository
import com.mbrainz.sample.ui.feature.artist.ArtistDetailViewModel
import com.mbrainz.sample.ui.feature.artist.ArtistViewState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistDetailViewModelTest {
    private val mockRepository = mockk<ArtistRepository>()
    private lateinit var viewModel: ArtistDetailViewModel

    @Before
    fun setUp() {
        // GIVEN
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ArtistDetailViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN nominal case WHEN search for Artist with Id THEN emit loading and result`() = runTest {
        // GIVEN
        coEvery { mockRepository.getArtistReleases(any()) } coAnswers {
            delay(200)
            TestFixtures.basicBandArtist
        }

        // WHEN
        viewModel.retrieveArtistInformation("sample")

        // THEN
        viewModel.artist.test {
            Assertions.assertThat(awaitItem() is ArtistViewState.Loading).isTrue
            val successState = awaitItem() // next item is result
            Assertions.assertThat(successState is ArtistViewState.Success).isTrue
            val result = (successState as ArtistViewState.Success).result
            Assertions.assertThat(result.releases.size).isEqualTo(1)
            Assertions.assertThat(result.id).isEqualTo(TestFixtures.artistId1)
        }
    }

    @Test
    fun `GIVEN error case WHEN search for Artist with Id THEN emit loading and error`() = runTest {
        // GIVEN
        coEvery { mockRepository.searchArtist(any()) } coAnswers {
            delay(200)
            throw Exception("oh noes")
        }

        // WHEN
        viewModel.retrieveArtistInformation("sample")

        // THEN
        viewModel.artist.test {
            Assertions.assertThat(awaitItem() is ArtistViewState.Loading).isTrue
            Assertions.assertThat(awaitItem() is ArtistViewState.Error).isTrue
        }
    }
}
