package com.mbrainz.sample.unit.data.manager

import com.mbrainz.sample.data.manager.MusicBrainzApiManagerImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MusicBrainzApiManagerImplTest {

    @Test
    fun `WHEN search for offspring THEN first result is appropriate`() = runTest {
        // GIVEN
        val apiManager = MusicBrainzApiManagerImpl()

        // WHEN
        val res = apiManager.searchArtist("offspring")

        // THEN
        assertThat(res.count).isGreaterThan(0)
        val topResult = res.artists.maxByOrNull { it.score }
        assertThat(topResult).isNotNull
        assertThat(topResult!!.name).isEqualTo("The Offspring")
    }

    @Test
    fun `WHEN retrieve artist releases THEN result is appropriate`() = runTest {
        // GIVEN
        val apiManager = MusicBrainzApiManagerImpl()

        // WHEN
        val res = apiManager.getArtistReleases("23a03e33-a603-404e-bcbf-2c00159d7067")

        // THEN
        assertThat(res).isNotNull
        assertThat(res.releases.size).isGreaterThan(0)
    }
}
