package org.jto.dupcheck.service.statistics

interface IDuplicateCheckServiceStatistics<T> {
    fun getStatistics(): DuplicateCheckStatisticsResponse
}