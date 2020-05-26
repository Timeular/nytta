package com.timeular.nytta.ihop

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

internal class MemAnalyzerTest {

    private val memAnalyzer = MemAnalyzer()

    @Test
    fun testToKiloBytes() {
        assertThat(1024L.toKiloBytes(), equalTo(1.0))
    }

    @Test
    fun testToMegaBytes() {
        assertThat(1048576L.toMegaBytes(), equalTo(1.0))
    }

    @Test
    fun testToGigaBytes() {
        assertThat(1073741824L.toGigaBytes(), equalTo(1.0))
    }

    @Test
    fun testDetectUnit() {
        val snapshot = MemAnalyzer.MemSnapshot(
                maxMemory = 1024,
                totalMemory = 1024,
                freeMemory = 1022,
                name = "Test"
        )

        assertThat(memAnalyzer.detectUnit(snapshot), equalTo(MemAnalyzer.Unit.Byte))
        assertThat(memAnalyzer.detectUnit(snapshot.copy(freeMemory = 0)), equalTo(MemAnalyzer.Unit.KByte))
        assertThat(memAnalyzer.detectUnit(snapshot.copy(freeMemory = (-1024 * 1024 + 1025))), equalTo(MemAnalyzer.Unit.KByte))
        assertThat(memAnalyzer.detectUnit(snapshot.copy(freeMemory = -1024 * 1024)), equalTo(MemAnalyzer.Unit.MByte))
        assertThat(memAnalyzer.detectUnit(snapshot.copy(freeMemory = -1024 * 1024 * 1024)), equalTo(MemAnalyzer.Unit.GByte))
    }
}