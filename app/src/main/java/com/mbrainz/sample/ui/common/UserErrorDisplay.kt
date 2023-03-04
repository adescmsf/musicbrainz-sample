package com.mbrainz.sample.ui.common

import android.content.Context
import android.widget.Toast
import com.mbrainz.sample.R
import com.mbrainz.sample.common.Logger

interface UserErrorDisplay {
    fun displayUserMessage(exception: Exception)
}

class UserErrorDisplayToastImpl(
    private val context: Context,
    private val logger: Logger
) : UserErrorDisplay {
    override fun displayUserMessage(exception: Exception) {
        logger.logInfo("Exception in UserErrorDisplayToastImpl : ${exception.stackTrace}")
        val message = context.resources.getString(R.string.error_default_user_message, exception.message)
        Toast
            .makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }
}