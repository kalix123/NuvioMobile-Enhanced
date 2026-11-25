package com.nuvio.app.features.player

import kotlin.test.Test
import kotlin.test.assertEquals

class PlayerSkipSecondsTest {
    @Test
    fun keepsPresetValues() {
        PlayerSkipSecondsOptions.forEach { assertEquals(it, normalizePlayerSkipSeconds(it)) }
    }

    @Test
    fun keepsCustomValuesInRange() {
        assertEquals(1, normalizePlayerSkipSeconds(1))
        assertEquals(3, normalizePlayerSkipSeconds(3))
        assertEquals(85, normalizePlayerSkipSeconds(85))
        assertEquals(600, normalizePlayerSkipSeconds(600))
    }

    @Test
    fun fallsBackToDefaultOutsideRange() {
        assertEquals(PLAYER_SKIP_SECONDS_DEFAULT, normalizePlayerSkipSeconds(null))
        assertEquals(PLAYER_SKIP_SECONDS_DEFAULT, normalizePlayerSkipSeconds(0))
        assertEquals(PLAYER_SKIP_SECONDS_DEFAULT, normalizePlayerSkipSeconds(-5))
        assertEquals(PLAYER_SKIP_SECONDS_DEFAULT, normalizePlayerSkipSeconds(601))
    }
}
