package org.jto.dupcheck.service

import java.time.LocalDateTime

data class Entry<T>(
    override val uniqueId: T,
    override val metadata: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
) : IBaseEntry<T>
