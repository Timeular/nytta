package com.timeular.nytta.ihop

import java.util.*

/**
 * Simple interface for generating UUIDs.
 *
 * The main purpose of this interface is to make testing easier (e.g. to provide a custom implementation)
 */
interface UUIDGenerator {
    fun generateUUID(): String
}


/**
 * Simple UUIDGenerator implementation which uses java UUIDs
 */
class SimpleUUIDGenerator : UUIDGenerator {
    override fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }
}