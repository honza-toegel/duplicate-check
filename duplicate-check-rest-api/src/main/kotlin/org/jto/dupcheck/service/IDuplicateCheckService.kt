package org.jto.dupcheck.service

interface IDuplicateCheckService<T> {
    fun check(request: DuplicateCheckRequest<T>): DuplicateCheckResponse<T>
}