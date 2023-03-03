package com.mbrainz.sample.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseEntity(
    @SerialName("id")
    val releaseId: String,
    @SerialName("date")
    val releaseDate: String = "",
    @SerialName("title")
    val name: String,
    @SerialName("status")
    val status: String = ""
)