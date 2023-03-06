package com.mbrainz.sample.unit.ui.feature.artist

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.mbrainz.sample.R
import com.mbrainz.sample.TestApplication
import com.mbrainz.sample.TestFixtures
import com.mbrainz.sample.ui.feature.artist.ArtistDetailFragment
import com.mbrainz.sample.ui.feature.artist.ArtistDetailFragmentArgs
import com.mbrainz.sample.ui.feature.artist.ArtistDetailViewModel
import com.mbrainz.sample.ui.feature.artist.ArtistViewState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class)
class ArtistDetailFragmentTest : AutoCloseKoinTest() {
    private lateinit var scenario: FragmentScenario<ArtistDetailFragment>
    private lateinit var mockViewModel: ArtistDetailViewModel
    private val fakeArtistStateFlow: MutableStateFlow<ArtistViewState> = MutableStateFlow(
        ArtistViewState.Error(Exception("empty"))
    )

    @Before
    fun setUp() {
        // GIVEN
        mockViewModel = mockk()
        every { mockViewModel.retrieveArtistInformation(any()) } returns Unit
        every { mockViewModel.artist } returns fakeArtistStateFlow
        val testModule = module(true) {
            single { mockViewModel }
        }
        loadKoinModules(testModule)
        val bundle = Bundle().apply {
            putString("artistId", "fake")
        }
        scenario = launchFragmentInContainer(
            fragmentArgs = ArtistDetailFragmentArgs.fromBundle(bundle).toBundle()
        )
    }

    @Test
    fun `WHEN fragment is loaded THEN viewModel loads the artistId`() {
        // THEN
        verify {
            mockViewModel.retrieveArtistInformation(any())
        }
    }

    @Test
    fun `WHEN viewmodel emits success state with result THEN result is displayed`() = runTest {
        // WHEN
        fakeArtistStateFlow.emit(ArtistViewState.Success(TestFixtures.basicBandArtist.copy(country = "")))

        // THEN
        assertNotDisplayed(R.id.fragment_detail_progress_bar)
        assertNotDisplayed(R.id.fragment_detail_error_textview)
        assertNotDisplayed(R.id.fragment_detail_retry_button)

        assertDisplayed(R.id.fragment_detail_card)
        assertDisplayed(R.id.fragment_detail_title, "Artist 1")
        assertDisplayed(R.id.fragment_detail_subtitle, "rock")
        assertDisplayed(R.id.fragment_detail_more, "Band artist • From Lyon • Since 1980")

        assertDisplayed(R.id.fragment_detail_header, "Official Releases (1)")
        assertDisplayedAtPosition(R.id.fragment_detail_releases_recycler_view, 0, R.id.item_result_release, "Release 1 (1980)")
    }

    @Test
    fun `WHEN viewmodel emits loading state THEN loader is displayed`() = runTest {
        // WHEN
        fakeArtistStateFlow.emit(ArtistViewState.Loading)

        // THEN
        assertDisplayed(R.id.fragment_detail_progress_bar)

        assertNotDisplayed(R.id.fragment_detail_error_textview)
        assertNotDisplayed(R.id.fragment_detail_retry_button)
        assertNotDisplayed(R.id.fragment_detail_header)
        assertNotDisplayed(R.id.fragment_detail_releases_recycler_view)
        assertNotDisplayed(R.id.fragment_detail_card)
    }

    @Test
    fun `WHEN viewmodel emits error state THEN error is displayed`() = runTest {
        // WHEN
        fakeArtistStateFlow.emit(ArtistViewState.Error(Exception("woops")))

        // THEN
        assertDisplayed(R.id.fragment_detail_error_textview)
        assertDisplayed(R.id.fragment_detail_retry_button)

        assertNotDisplayed(R.id.fragment_detail_header)
        assertNotDisplayed(R.id.fragment_detail_releases_recycler_view)
        assertNotDisplayed(R.id.fragment_detail_card)
        assertNotDisplayed(R.id.fragment_detail_progress_bar)
    }
}
