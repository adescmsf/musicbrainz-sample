package com.mbrainz.sample

import com.mbrainz.sample.data.model.Artist
import com.mbrainz.sample.data.model.ArtistType
import com.mbrainz.sample.data.model.Release
import com.mbrainz.sample.data.model.remote.ArtistEntity
import com.mbrainz.sample.data.model.remote.BeginAreaEntity
import com.mbrainz.sample.data.model.remote.LifeSpanEntity
import com.mbrainz.sample.data.model.remote.ReleaseEntity

object TestFixtures {
    const val artistId1 = "a1"
    const val artistId2 = "a2"

    const val release1Name = "Release 1"

    private val basicReleaseEntity = ReleaseEntity(
        releaseId = "r1",
        name = release1Name,
        status = "Official"
    )

    private val notOfficialReleaseEntity = ReleaseEntity(
        releaseId = "r2",
        name = "Release 2",
    )
    private val basicLifeSpanEntity = LifeSpanEntity(begin = "1999")
    private val basicBeginAreaEntity = BeginAreaEntity(name = "Lyon")

    val basicBandArtistEntity = ArtistEntity(
        artistId = artistId1,
        name = "Artist 1",
        disambiguation = "rock",
        type = "BAND",
        country = "FR",
        score = 100,
        lifeSpan = basicLifeSpanEntity,
        beginArea = basicBeginAreaEntity,
        releases = listOf(basicReleaseEntity, notOfficialReleaseEntity)
    )

    val basicSoloArtistEntity = basicBandArtistEntity.copy(
        artistId = artistId2,
        name = "Artist 2",
        type = "PERSON"
    )
    val basicUnknownArtistEntity = ArtistEntity(
        artistId = "a3",
        name = "Artist 3",
    )
    private val basicRelease = Release(
        name = release1Name,
        releaseDate = "1980"
    )
    val basicBandArtist = Artist(
        name = "Artist 1",
        genre = "rock",
        type = ArtistType.BAND,
        country = "FR",
        creationYear = "1980",
        matchScore = 100,
        releases = listOf(basicRelease),
        origin = "Lyon",
        id = artistId1
    )
}
