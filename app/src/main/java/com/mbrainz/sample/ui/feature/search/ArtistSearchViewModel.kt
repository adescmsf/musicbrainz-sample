package com.mbrainz.sample.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbrainz.sample.data.ArtistRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistSearchViewModel(
    private val repository: ArtistRepository
) : ViewModel() {
    private val artist = MutableSharedFlow<String>(replay = 1)
    val searchedArtist = artist.transformLatest { artist ->
        if (artist.length <= 2) {
            emit(ArtistSearchState.Success(emptyList()))
            return@transformLatest
        }
        emit(ArtistSearchState.Loading)
        try {
            val result = repository.searchArtist(artist)
            emit(ArtistSearchState.Success(result))
        } catch (_: CancellationException) {
            // cancellation exceptions are expected if we flood the search (only the last search is relevant)
        } catch (exception: Exception) {
            emit(ArtistSearchState.Error(exception))
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ArtistSearchState.Success(emptyList()))

    fun searchArtist(searchTerm: String) {
        viewModelScope.launch {
            artist.emit(searchTerm)
        }
    }
}
