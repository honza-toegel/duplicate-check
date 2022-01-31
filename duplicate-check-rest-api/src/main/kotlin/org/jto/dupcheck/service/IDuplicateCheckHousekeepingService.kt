package org.jto.dupcheck.service

interface IDuplicateCheckHousekeepingService<T> {
    fun houseKeeping(olderAsDays: Long)
}