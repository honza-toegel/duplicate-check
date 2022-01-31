package org.jto.dupcheck.service

data class DuplicateCheckResponse<T>(val alreadyUsed: Boolean, val pastUsage: Entry<T>?)

fun<T> noDuplicateFound() = DuplicateCheckResponse<T>(false, null)
