package com.mbrainz.sample.data.model.mapper

import com.mbrainz.sample.data.model.Artist
import com.mbrainz.sample.data.model.ArtistType
import com.mbrainz.sample.data.model.Release
import com.mbrainz.sample.data.model.remote.ArtistEntity
import com.mbrainz.sample.data.model.remote.BeginAreaEntity
import com.mbrainz.sample.data.model.remote.LifeSpanEntity
import com.mbrainz.sample.data.model.remote.ReleaseEntity

object ArtistMapper {
    private const val filterReleaseStatus = "Official"
    private const val scoreFilter = 20

    fun transformArtistsEntity(artistsEntity: List<ArtistEntity>) =
        artistsEntity
            .filter { it.score > scoreFilter }
            .map { transformArtistEntity(it) }

    fun transformArtistEntity(artistEntity: ArtistEntity) = Artist(
        id = artistEntity.artistId,
        name = artistEntity.name,
        origin = transformBeginAreaEntity(artistEntity.beginArea),
        country = artistEntity.country,
        matchScore = artistEntity.score,
        creationYear = transformLifeSpanEntity(artistEntity.lifeSpan),
        genre = artistEntity.disambiguation,
        type = when (artistEntity.type) {
            ArtistType.BAND.value -> { ArtistType.BAND }
            ArtistType.PERSON.value -> { ArtistType.PERSON }
            else -> { ArtistType.UNKNOWN }
        },
        releases = artistEntity.releases
            .filter { it.status == filterReleaseStatus }
            .map { transformReleaseEntity(it) }
    )

    private fun transformLifeSpanEntity(lifeSpanEntity: LifeSpanEntity?) =
        lifeSpanEntity?.begin ?: ""
    private fun transformBeginAreaEntity(beginAreaEntity: BeginAreaEntity?) =
        beginAreaEntity?.name ?: ""
    private fun transformReleaseEntity(releaseEntity: ReleaseEntity) = Release(
        name = releaseEntity.name,
        releaseDate = releaseEntity.releaseDate
    )
}