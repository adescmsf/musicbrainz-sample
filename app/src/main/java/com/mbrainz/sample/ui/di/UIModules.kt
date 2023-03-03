package com.mbrainz.sample.ui.di

import com.mbrainz.sample.common.AppDispatchers
import com.mbrainz.sample.common.LocalLogger
import com.mbrainz.sample.common.Logger
import com.mbrainz.sample.ui.feature.artist.ArtistDetailViewModel
import com.mbrainz.sample.ui.feature.search.ArtistSearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

private val applicationModules = module {
    factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
    single<Logger> { LocalLogger() }
}

private val viewModelModule = module {
    viewModel { ArtistSearchViewModel(get()) }
}

val uiModules = arrayOf(applicationModules, viewModelModule)