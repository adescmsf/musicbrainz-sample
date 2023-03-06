package com.mbrainz.sample.data.model

import com.mbrainz.sample.common.countryCodeToUnicodeFlag
import java.util.Locale

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
        val emoji = countryEmoji()
        return if (creationYear.isEmpty()) {
            "$emoji $name".trim()
        } else {
            "$emoji $name ($creationYear)".trim()
        }
    }

    // TODO: should be i18n
    // TODO: unit tests should cover every cases
    fun allInformation(): String {
        val information = mutableListOf<String>()
        if (country.isNotEmpty()) information.add(countryEmoji())
        if (bandType().isNotEmpty()) information.add(bandType())
        if (origin.isNotEmpty()) information.add("From $origin")
        if (creationYear.isNotEmpty()) information.add("Since $creationYear")
        if (information.isEmpty()) return ""
        return information.joinToString(" • ")
    }

    private fun countryEmoji(): String {
        val validCountryList = Locale.getISOCountries().toList()
        return if (country.isNotEmpty() && validCountryList.contains(country)) country.countryCodeToUnicodeFlag() else ""
    }

    // TODO: should be i18n
    private fun bandType() = when (type) {
        ArtistType.PERSON -> "Solo artist"
        ArtistType.BAND -> "Band artist"
        else -> ""
    }
}
