package com.mbrainz.sample.data.model

import com.mbrainz.sample.ui.common.countryCodeToUnicodeFlag
import java.util.*


enum class ArtistType(val value: String) {
    PERSON("PERSON"),
    BAND("BAND"),
    UNKNOWN("UNKNOWN")
}

data class Artist(
    val id: String,
    val name: String,
    val origin: String,
    val country: String,
    val matchScore: Int,
    val creationYear: String,
    val genre: String,
    val type: ArtistType,
    val releases: List<Release>
) {
    fun fullArtistName(): String {
        val validCountryList = Locale.getISOCountries().toList()
        val emoji = if (country.isNotEmpty() && validCountryList.contains(country)) country.countryCodeToUnicodeFlag() else ""
        return if (creationYear.isEmpty()) {
            "$emoji $name".trim()
        } else {
            "$emoji $name (${creationYear})".trim()
        }
    }
}