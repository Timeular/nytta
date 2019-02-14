package com.timeular.nytta.tracking

/**
 * Root exception for the tracking package
 */
open class TrackerException(message: String, ex: Throwable? = null) : RuntimeException(message, ex)