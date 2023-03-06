package com.mbrainz.sample.unit.ui.feature.search

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.mbrainz.sample.R
import com.mbrainz.sample.TestApplication
import com.mbrainz.sample.TestFixtures
import com.mbrainz.sample.ui.feature.search.ArtistSearchFragment
import com.mbrainz.sample.ui.feature.search.ArtistSearchState
import com.mbrainz.sample.ui.feature.search.ArtistSearchViewModel
import io.mockk.every
import io.mockk.mockk
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
class ArtistSearchFragmentTest : AutoCloseKoinTest() {
    private lateinit var scenario: FragmentScenario<ArtistSearchFragment>
    private val fakeSearchStateFlow: MutableStateFlow<ArtistSearchState> = MutableStateFlow(ArtistSearchState.Success(emptyList()))

    @Before
    fun setUp() {
        // GIVEN
        val viewModel = mockk<ArtistSearchViewModel>()
        every { viewModel.searchArtist(any()) } returns Unit
        every { viewModel.searchedArtist } returns fakeSearchStateFlow
        val testModule = module(true) {
            single { viewModel }
        }
        loadKoinModules(testModule)
        scenario = launchFragmentInContainer()
    }

    @Test
    fun `WHEN first startup THEN only the form is displayed`() {
        // THEN
        assertDisplayed(R.id.fragment_search_string_layout)

        assertNotDisplayed(R.id.fragment_search_progress_bar)
        assertNotDisplayed(R.id.fragment_search_result_recycler_view)
        assertNotDisplayed(R.id.fragment_search_rv_header)
        assertNotDisplayed(R.id.fragment_search_error_textview)
        assertNotDisplayed(R.id.fragment_search_retry_button)
    }

    @Test
    fun `WHEN viewmodel emits success state with result THEN results are displayed`() = runTest {
        // WHEN
        fakeSearchStateFlow.emit(ArtistSearchState.Success(listOf(TestFixtures.basicBandArtist)))

        // THEN
        assertNotDisplayed(R.id.fragment_search_progress_bar)
        assertNotDisplayed(R.id.fragment_search_error_textview)
        assertNotDisplayed(R.id.fragment_search_retry_button)

        assertDisplayed(R.id.fragment_search_rv_header, "Results (1)")
        assertDisplayedAtPosition(R.id.fragment_search_result_recycler_view, 0, R.id.item_search_artist_name, "\uD83C\uDDEB\uD83C\uDDF7 Artist 1 (1980)")
    }

    @Test
    fun `WHEN viewmodel emits success state with empty results THEN nothing is displayed`() = runTest {
        // THEN
        assertDisplayed(R.id.fragment_search_string_layout)

        assertNotDisplayed(R.id.fragment_search_progress_bar)
        assertNotDisplayed(R.id.fragment_search_result_recycler_view)
        assertNotDisplayed(R.id.fragment_search_rv_header)
        assertNotDisplayed(R.id.fragment_search_error_textview)
        assertNotDisplayed(R.id.fragment_search_retry_button)
    }

    @Test
    fun `WHEN viewmodel emits loading state THEN loader is displayed`() = runTest {
        // WHEN
        fakeSearchStateFlow.emit(ArtistSearchState.Loading)

        // THEN
        assertDisplayed(R.id.fragment_search_progress_bar)

        assertNotDisplayed(R.id.fragment_search_result_recycler_view)
        assertNotDisplayed(R.id.fragment_search_rv_header)
        assertNotDisplayed(R.id.fragment_search_error_textview)
        assertNotDisplayed(R.id.fragment_search_retry_button)
    }

    @Test
    fun `WHEN viewmodel emits error state THEN error is displayed`() = runTest {
        // WHEN
        fakeSearchStateFlow.emit(ArtistSearchState.Error(Exception("Ooops")))

        // THEN
        assertDisplayed(R.id.fragment_search_error_textview)
        assertDisplayed(R.id.fragment_search_retry_button)

        assertNotDisplayed(R.id.fragment_search_result_recycler_view)
        assertNotDisplayed(R.id.fragment_search_rv_header)
        assertNotDisplayed(R.id.fragment_search_progress_bar)
    }
}
