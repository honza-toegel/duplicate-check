package org.jto.dupcheck.service

import org.jto.dupcheck.AppVersionInfo
import org.jto.dupcheck.service.statistics.DuplicateCheckStatisticsResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class StringDuplicateCheckRest(val duplicateCheck: DuplicateCheckService<String>,
                               @Value("\${build.revision}") val buildVersion: String,
                               @Value("\${build.timestamp}") val buildTimestamp: String,
                               @Value("\${housekeeping.older-than-days:30}")
                               private val houseKeepOlderThanDays: Long, ) {
    @PostMapping
    fun checkDuplicate(@RequestBody entry: DuplicateCheckRequest<String>): DuplicateCheckResponse<String> = duplicateCheck.check(entry)

    @GetMapping("version")
    fun getAppVersion(): AppVersionInfo = AppVersionInfo(buildVersion, buildTimestamp)

    @GetMapping("statistics")
    fun getStatistics(): DuplicateCheckStatisticsResponse = duplicateCheck.getStatistics()

    @Scheduled(cron = "0 0 1 * * *")
    fun houseKeeping() = duplicateCheck.houseKeeping(houseKeepOlderThanDays)
}