package com.mbrainz.sample.integration.data

import com.mbrainz.sample.common.AppDispatchers
import com.mbrainz.sample.data.ArtistRepository
import com.mbrainz.sample.data.manager.MusicBrainzApiManagerImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistRepositoryTest {
    private lateinit var artistRepository: ArtistRepository

    @Before
    fun setUp() {
        // GIVEN
        val apiManager = MusicBrainzApiManagerImpl()
        val appDispatchers = AppDispatchers(Dispatchers.Unconfined, Dispatchers.Unconfined)
        artistRepository = ArtistRepository(apiManager, appDispatchers)
    }

    @Test
    fun `WHEN existing artist is searched THEN appropriate result is returned`() = runTest {
        // WHEN
        val res = artistRepository.searchArtist("offspring")

        // THEN
        assertThat(res.size).isGreaterThan(0)
        assertThat(res[0].matchScore).isEqualTo(100)
        assertThat(res[0].country).isEqualTo("US")
        assertThat(res[0].creationYear).isEqualTo("1984")
    }

    @Test
    fun `WHEN bogus artist is searched THEN nothing is returned`() = runTest {
        // WHEN
        val res = artistRepository.searchArtist("zzxzzxzp")

        // THEN
        assertThat(res.size).isEqualTo(0)
    }

    @Test
    fun `WHEN existing artist releases are searched THEN appropriate result is returned`() = runTest {
        // WHEN
        val res = artistRepository.getArtistReleases("23a03e33-a603-404e-bcbf-2c00159d7067")

        // THEN
        assertThat(res).isNotNull
        assertThat(res.releases.size).isGreaterThan(0)
    }
}
