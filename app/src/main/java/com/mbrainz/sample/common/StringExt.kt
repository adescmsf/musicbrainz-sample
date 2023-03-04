package com.mbrainz.sample.ui.common

/**
 * Converts an ISO 3166-1 alpha-2 country code to the corresponding Unicode flag emoji.
 * Gist from https://gist.github.com/outadoc/ab3b8da157748183d4afca5de4a26a54
 */
fun String.countryCodeToUnicodeFlag(): String {
    return this
        .filter { it in 'A'..'Z' }
        .map { it.toByte() }
        .flatMap { char ->
            listOf(
                // First UTF-16 char is \uD83C
                0xD8.toByte(),
                0x3C.toByte(),
                // Second char is \uDDE6 for A and increments from there
                0xDD.toByte(),
                (0xE6.toByte() + (char - 'A'.toByte())).toByte()
            )
        }
        .toByteArray()
        .let { bytes ->
            String(bytes, Charsets.UTF_16)
        }
}

fun String.trimChar(nbChars: Int) : String {
    if (this.length <= nbChars) return this
    return this.take(nbChars).trim() + "..."
}
