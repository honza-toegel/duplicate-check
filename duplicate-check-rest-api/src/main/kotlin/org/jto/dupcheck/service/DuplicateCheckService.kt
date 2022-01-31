package org.jto.dupcheck.service

import org.jto.dupcheck.service.statistics.DuplicateCheckStatisticsResponse
import org.jto.dupcheck.service.statistics.IDuplicateCheckServiceStatistics
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Service
class DuplicateCheckService<T> : IDuplicateCheckService<T>, IDuplicateCheckHousekeepingService<T>,
    IDuplicateCheckServiceStatistics<T> {
    private val entries: MutableMap<T, Entry<T>> = ConcurrentHashMap()
    override fun check(request: DuplicateCheckRequest<T>): DuplicateCheckResponse<T> {
        logger.info("Checking entry $request")
        val existingEntry = entries.putIfAbsent(request.uniqueId, Entry(request.uniqueId, request.metadata))
        return if (existingEntry != null) {
            logger.info("Entry $request already exists (the duplicate of $existingEntry)")
            DuplicateCheckResponse(true, existingEntry)
        } else {
            logger.info("Entry $request is unique, and was stored")
            DuplicateCheckResponse(false, null)
        }
    }

    override fun houseKeeping(olderAsDays: Long) {
        val olderAsTimestamp = LocalDateTime.now().minusDays(olderAsDays)
        logger.info("Housekeeping all entities older than $olderAsDays days (created before $olderAsTimestamp)")
        entries.values.removeIf { it.timestamp < olderAsTimestamp }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(DuplicateCheckService::class.java)
    }

    override fun getStatistics(): DuplicateCheckStatisticsResponse {
        return DuplicateCheckStatisticsResponse(
            entries.size,
            entries.values.minOfOrNull { it.timestamp },
            entries.values.maxOfOrNull { it.timestamp })
    }
}
