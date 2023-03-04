package com.mbrainz.sample.data.model

data class Release(
    val name: String,
    val releaseDate: String
) {
    fun fullName(): String =
        if (releaseDate.isEmpty()) name
        else "$name ($releaseDate)"
}
