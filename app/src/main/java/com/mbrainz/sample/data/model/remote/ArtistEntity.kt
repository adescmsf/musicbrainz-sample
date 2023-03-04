package com.mbrainz.sample.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchArtistsResponse(
    val count: Int,
    val artists: List<ArtistEntity>
)

@Serializable
data class LifeSpanEntity(
    val begin: String = "",
    val end: String? = null
)

@Serializable
data class BeginAreaEntity(
    val name: String = ""
)

@Serializable
data class ArtistEntity(
    @SerialName("id")
    val artistId: String,
    @SerialName("name")
    val name: String,
    @SerialName("disambiguation")
    val disambiguation: String = "",
    @SerialName("type")
    val type: String = "",
    @SerialName("country")
    val country: String = "",
    @SerialName("score")
    val score: Int = 0,
    @SerialName("life-span")
    val lifeSpan: LifeSpanEntity? = null,
    @SerialName("begin-area")
    val beginArea: BeginAreaEntity? = null,
    @SerialName("releases")
    val releases: List<ReleaseEntity> = emptyList()
)
