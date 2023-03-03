package com.mbrainz.sample.common

import timber.log.Timber

interface Logger {
    fun logInfo(message: String)
}
class LocalLogger : Logger {
    init {
        Timber.plant(Timber.DebugTree())
    }

    override fun logInfo(message: String) = Timber.i(message)
}