package com.mbrainz.sample.data.di

import com.mbrainz.sample.data.ArtistRepository
import com.mbrainz.sample.data.manager.MusicBrainzApiManager
import com.mbrainz.sample.data.manager.MusicBrainzApiManagerImpl
import org.koin.dsl.module

private val managerModule = module {
    single<MusicBrainzApiManager> { MusicBrainzApiManagerImpl() }
}

private val repositoryModule = module {
    factory { ArtistRepository(get(), get()) }
}

val dataModules = arrayOf(managerModule, repositoryModule)