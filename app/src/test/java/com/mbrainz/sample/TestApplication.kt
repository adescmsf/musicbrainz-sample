package com.mbrainz.sample

import android.app.Application
import android.util.Log
import com.mbrainz.sample.data.di.dataModules
import com.mbrainz.sample.ui.di.uiModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_Mbrainzsample)
        try {
            startKoin {
                androidContext(this@TestApplication)
                modules((dataModules + uiModules).toList())
            }
        } catch (e: IllegalStateException) {
            Log.e("TestApp", "Koin initialization exception: ", e)
        }
    }
}
