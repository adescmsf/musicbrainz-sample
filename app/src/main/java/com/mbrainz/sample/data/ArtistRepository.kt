package com.mbrainz.sample.data

import com.mbrainz.sample.common.AppDispatchers
import com.mbrainz.sample.data.manager.MusicBrainzApiManager
import com.mbrainz.sample.data.model.mapper.ArtistMapper
import kotlinx.coroutines.withContext

class ArtistRepository(
    private val musicBrainzApiManager: MusicBrainzApiManager,
    private val appDispatchers: AppDispatchers
) {
    suspend fun searchArtist(artistName: String) = withContext(appDispatchers.io) {
        val artists = ArtistMapper.transformArtistsEntity(musicBrainzApiManager.searchArtist(artistName).artists)
        artists.sortedByDescending { it.matchScore }
    }

    suspend fun getArtistReleases(artistId: String) = withContext(appDispatchers.io) {
        ArtistMapper.transformArtistEntity(musicBrainzApiManager.getArtistReleases(artistId))
    }
}