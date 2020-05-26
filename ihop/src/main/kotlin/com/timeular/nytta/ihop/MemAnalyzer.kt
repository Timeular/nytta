package com.timeular.nytta.ihop

import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Small helper to analyze memory roughly within the code
 */
class MemAnalyzer {

    private val runtime = Runtime.getRuntime()
    private val listMemorySnapshots: MutableList<MemSnapshot> = mutableListOf()
    private var currentUnit: Unit = Unit.GByte

    fun snapshot(name: String) {
        val snapshot = takeMemorySnapshot(name)
        listMemorySnapshots.add(snapshot)
        val snapshotUnit = detectUnit(snapshot)
        if (currentUnit > snapshotUnit) {
            currentUnit = snapshotUnit
        }
    }

    fun reset() {
        listMemorySnapshots.clear()
        currentUnit = Unit.GByte
    }

    fun shortSummary(): String =
            when (listMemorySnapshots.size) {
                0 -> "You have to call snapshot if you want to analyze something"
                1 -> createMemoryLine(listMemorySnapshots.first())
                else -> createSummary(listOf(listMemorySnapshots.first(), listMemorySnapshots.last()))
            }

    fun detailedSummary(): String =
            when (listMemorySnapshots.size) {
                0 -> "You have to call snapshot if you want to analyze something"
                1 -> createMemoryLine(listMemorySnapshots.first())
                else -> createSummary(listMemorySnapshots)
            }

    private fun createSummary(snapshots: List<MemSnapshot>): String {
        val first = snapshots.first()
        val last = snapshots.last()

        val sb = StringBuilder()
        sb.append("Max Memory: ${first.maxMemory.toStringWithUnit()}\n")

        snapshots.forEach { mem ->
            sb.append(createMemoryLine(mem))
        }

        sb.append("-".repeat(15))
        sb.append("\n")
        val timeDelta = last.time.toEpochSecond(ZoneOffset.UTC) - first.time.toEpochSecond(ZoneOffset.UTC)
        val totalDelta = last.totalMemory - first.totalMemory
        val freeDelta = last.freeMemory - first.freeMemory
        val usedDelta = (totalDelta - freeDelta).toStringWithUnit()
        sb.append("Delta .... Duration: ${timeDelta}s".padEnd(50, ' '))
        sb.append(
                "Total Memory: %10s\tUsed Memory: %10s\tFree Memory: %10s\n".format(
                        totalDelta.toStringWithUnit(),
                        usedDelta,
                        freeDelta.toStringWithUnit()
                )
        )

        return sb.toString()
    }

    private fun createMemoryLine(mem: MemSnapshot): String {
        val total = mem.totalMemory.toStringWithUnit()
        val free = mem.freeMemory.toStringWithUnit()
        val used = mem.usedMemory.toStringWithUnit()

        return "%sTotal Memory: %10s\tUsed Memory: %10s\tFree Memory: %10s\n".format(
                "${mem.name}(${mem.time}):".padEnd(
                        50,
                        ' '
                ),
                total,
                used,
                free
        )
    }

    private fun takeMemorySnapshot(name: String): MemSnapshot {
        runtime.gc()
        return MemSnapshot(
                name = name,
                maxMemory = runtime.maxMemory(),
                freeMemory = runtime.freeMemory(),
                totalMemory = runtime.totalMemory()
        )
    }

    internal fun detectUnit(snapshot: MemSnapshot): Unit {
        val used = snapshot.usedMemory

        if (used < 1024) {
            return Unit.Byte
        }

        if (used.toKiloBytes() < 1024) {
            return Unit.KByte
        }

        if (used.toMegaBytes() < 1024) {
            return Unit.MByte
        }

        if (used.toGigaBytes() < 1024) {
            return Unit.GByte
        }

        return Unit.Byte
    }

    private fun Long.toStringWithUnit(): String =
            when (currentUnit) {
                Unit.Byte -> "$this B"
                Unit.KByte -> "%.2f KB".format(this.toKiloBytes())
                Unit.MByte -> "%.2f MB".format(this.toMegaBytes())
                Unit.GByte -> "%.2f GB".format(this.toGigaBytes())
            }

    data class MemSnapshot(
            val name: String,
            val time: LocalDateTime = LocalDateTime.now(),
            val maxMemory: Long,
            val freeMemory: Long,
            val totalMemory: Long
    ) {
        val usedMemory: Long
            get() = totalMemory - freeMemory
    }

    enum class Unit {
        Byte,
        KByte,
        MByte,
        GByte;

    }
}

internal fun Long.toKiloBytes(): Double = this / 1024.0
internal fun Long.toMegaBytes(): Double = this / 1024.0 / 1024
internal fun Long.toGigaBytes(): Double = this / 1024.0 / 1024 / 1024