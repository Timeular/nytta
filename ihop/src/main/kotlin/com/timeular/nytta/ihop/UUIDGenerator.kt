package com.timeular.nytta.ihop

import java.util.*


interface UUIDGenerator {
    fun generateUUID(): String
}

class SimpleUUIDGenerator : UUIDGenerator {
    override fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }
}