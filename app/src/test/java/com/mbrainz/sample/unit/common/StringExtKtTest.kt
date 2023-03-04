package com.mbrainz.sample.unit.common

import com.mbrainz.sample.ui.common.trimChar
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class StringExtKtTest {
    @Test
    fun `GIVEN long string THEN string is trimmed`() {
        assertThat("This is a long string".trimChar(5)).isEqualTo("This...")
    }

    @Test
    fun `GIVEN short string THEN string is the same`() {
        assertThat("This is a long string".trimChar(22)).isEqualTo("This is a long string")
    }
}
