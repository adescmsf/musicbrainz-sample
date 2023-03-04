package com.mbrainz.sample.ui.feature.artist

import com.mbrainz.sample.data.model.Artist

sealed class ArtistViewState {
    object Loading : ArtistViewState()
    data class Success(val result: Artist) : ArtistViewState()
    data class Error(val error: Exception) : ArtistViewState()
}
