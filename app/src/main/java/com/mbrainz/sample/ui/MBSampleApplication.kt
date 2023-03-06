package com.mbrainz.sample.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.mbrainz.sample.data.di.dataModules
import com.mbrainz.sample.ui.di.uiModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MBSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // TODO: should be handled in the future
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidContext(this@MBSampleApplication)
            modules((uiModules + dataModules).toList())
        }
    }
}
