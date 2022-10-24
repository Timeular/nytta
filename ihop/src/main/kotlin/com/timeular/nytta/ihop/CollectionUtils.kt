package com.timeular.nytta.ihop

fun <T> Collection<T>.executeIfNotEmpty(applyFn: (Collection<T>) -> Unit) {
    if (this.isNotEmpty()) {
        applyFn(this)
    }
}
