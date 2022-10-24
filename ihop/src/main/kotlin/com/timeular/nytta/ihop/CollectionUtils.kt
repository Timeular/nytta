package com.timeular.nytta.ihop

fun <T> Collection<T>.executeIfNotEmpty(applyFn: (Collection<T>) -> Unit) {
    if (this.isNotEmpty()) {
        applyFn(this)
    }
}

fun <K, V> Map<K, V>.executeIfNotEmpty(applyFn: (Map<K, V>) -> Unit) {
    if (this.isNotEmpty()) {
        applyFn(this)
    }
}
