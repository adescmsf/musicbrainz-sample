package com.mbrainz.sample.ui.feature.search

import com.mbrainz.sample.data.model.Artist

sealed class ArtistSearchState {
    object Loading : ArtistSearchState()
    data class Success(val result: List<Artist>) : ArtistSearchState()
    data class Error(val error: Exception) : ArtistSearchState()
}
