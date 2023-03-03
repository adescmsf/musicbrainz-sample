package com.mbrainz.sample.data.manager

import com.mbrainz.sample.data.model.remote.ArtistEntity
import com.mbrainz.sample.data.model.remote.SearchArtistsResponse

interface MusicBrainzApiManager {
    suspend fun searchArtist(searchTerm: String): SearchArtistsResponse
    suspend fun getArtistReleases(artistId: String): ArtistEntity
}