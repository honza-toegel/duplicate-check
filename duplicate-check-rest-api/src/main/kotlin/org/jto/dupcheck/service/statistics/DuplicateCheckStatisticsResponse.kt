package org.jto.dupcheck.service.statistics

import java.time.LocalDateTime

data class DuplicateCheckStatisticsResponse(val numberOfEntries: Int, val oldestEntryTimestamp: LocalDateTime?, val newestEntryTimestamp: LocalDateTime?)
