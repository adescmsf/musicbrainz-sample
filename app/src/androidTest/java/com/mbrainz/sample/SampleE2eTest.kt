package com.mbrainz.sample

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.mbrainz.sample.ui.MainActivity
import org.awaitility.Awaitility
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@LargeTest
@RunWith(AndroidJUnit4::class)
class SampleE2eTest {
    @get:Rule
    val activityScenario = activityScenarioRule<MainActivity>()

    @Test
    fun basicEndToEndTest() {
        // Search for band
        writeTo(R.id.fragment_search_string_edit_text, "Offspring")

        // Click on first result
        Awaitility.await().pollDelay(1, TimeUnit.SECONDS).atMost(3, TimeUnit.SECONDS).untilAsserted {
            clickListItem(R.id.fragment_search_result_recycler_view, 0)
        }

        // Assert that detail fragment is displayed
        assertDisplayed(R.id.fragment_detail_card)
        assertDisplayed(R.id.fragment_detail_releases_recycler_view)
    }
}
