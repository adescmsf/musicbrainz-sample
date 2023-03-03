package com.mbrainz.sample.data

import com.mbrainz.sample.common.AppDispatchers
import com.mbrainz.sample.data.manager.MusicBrainzApiManager
import com.mbrainz.sample.data.model.parser.ArtistParser
import kotlinx.coroutines.withContext

class ArtistRepository(
    private val musicBrainzApiManager: MusicBrainzApiManager,
    private val appDispatchers: AppDispatchers
) {
    suspend fun searchArtist(artistName: String) = withContext(appDispatchers.io) {
        val artists = ArtistParser.transformArtistsEntity(musicBrainzApiManager.searchArtist(artistName).artists)
        artists.sortedByDescending { it.matchScore }
    }

    suspend fun getArtistReleases(artistId: String) = withContext(appDispatchers.io) {
        ArtistParser.transformArtistEntity(musicBrainzApiManager.getArtistReleases(artistId))
    }
}