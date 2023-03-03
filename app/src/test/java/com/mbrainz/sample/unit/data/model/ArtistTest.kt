package com.mbrainz.sample.unit.data.model

import com.mbrainz.sample.TestFixtures
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ArtistTest {
    @Test
    fun `GIVEN valid country code and creationYear WHEN fullArtistName called THEN emoji is added`() {
        val artist = TestFixtures.basicBandArtist
        assertThat(artist.fullArtistName()).isEqualTo("\uD83C\uDDEB\uD83C\uDDF7 Artist 1 (1980)")
    }

    @Test
    fun `GIVEN invalid country code and creationYear WHEN fullArtistName called THEN no emoji is added`() {
        val artist1 = TestFixtures.basicBandArtist.copy(country = "USFRUK")
        val artist2 = TestFixtures.basicBandArtist.copy(country = "ZZ")
        assertThat(artist1.fullArtistName()).isEqualTo("Artist 1 (1980)")
        assertThat(artist2.fullArtistName()).isEqualTo("Artist 1 (1980)")
    }

    @Test
    fun `GIVEN no country code and creationYear WHEN fullArtistName called THEN no emoji is added`() {
        val artist = TestFixtures.basicBandArtist.copy(country = "")
        assertThat(artist.fullArtistName()).isEqualTo("Artist 1 (1980)")
    }

    @Test
    fun `GIVEN no country code & no creationYear WHEN fullArtistName called THEN only band name is returned`() {
        val artist = TestFixtures.basicBandArtist.copy(country = "", creationYear = "")
        assertThat(artist.fullArtistName()).isEqualTo("Artist 1")
    }
}