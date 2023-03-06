package com.mbrainz.sample.unit.data.model

import com.mbrainz.sample.TestFixtures
import com.mbrainz.sample.data.model.ArtistType
import com.mbrainz.sample.data.model.mapper.ArtistMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ArtistMapperTest {
    @Test
    fun `WHEN artist entity parsed THEN all business rules are applied`() {
        // WHEN
        val res = ArtistMapper.transformArtistsEntity(
            listOf(TestFixtures.basicBandArtistEntity, TestFixtures.basicSoloArtistEntity, TestFixtures.basicUnknownArtistEntity)
        )
        // THEN
        // assert that artists with score < 30 are excluded
        assertThat(res.size).isEqualTo(2)

        assertThat(res[0].id).isEqualTo(TestFixtures.artistId1)
        assertThat(res[0].type).isEqualTo(ArtistType.BAND)
        // assert that releases which are not "Official" are excluded
        assertThat(res[0].releases.size).isEqualTo(1)
        assertThat(res[0].releases[0].name).isEqualTo(TestFixtures.release1Name)
        assertThat(res[1].id).isEqualTo(TestFixtures.artistId2)
        assertThat(res[1].type).isEqualTo(ArtistType.PERSON)
    }
}
