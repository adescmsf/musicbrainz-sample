package com.mbrainz.sample.ui.feature.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbrainz.sample.data.ArtistRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistDetailViewModel(
    private val repository: ArtistRepository
) : ViewModel() {
    private val artistId = MutableSharedFlow<String>(replay = 1)
    val artist = artistId.transformLatest { artistId ->
        emit(ArtistViewState.Loading)
        try {
            val result = repository.getArtistReleases(artistId)
            emit(ArtistViewState.Success(result))
        } catch (_: CancellationException) {
            // cancellation exceptions are expected if we flood the search (only the last search is relevant)
        } catch (exception: Exception) {
            emit(ArtistViewState.Error(exception))
        }
    }

    fun retrieveArtistInformation(id: String) {
        viewModelScope.launch {
            artistId.emit(id)
        }
    }
}
