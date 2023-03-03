package com.mbrainz.sample.ui.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbrainz.sample.data.ArtistRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistSearchViewModel(
    private val repository: ArtistRepository
) : ViewModel() {
    private val artist = MutableSharedFlow<String>(replay = 1)
    val searchedArtist = artist.transformLatest { artist ->
        emit(ArtistSearchState.Loading)
        try {
            val result = repository.searchArtist(artist)
            emit(ArtistSearchState.Success(result))
        } catch (exception: Exception) {
            emit(ArtistSearchState.Error(exception))
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ArtistSearchState.Success(emptyList()))

    fun searchArtist(searchTerm: String) = viewModelScope.launch {
        artist.emit(searchTerm)
    }
}