package org.jto.dupcheck.service

interface IBaseEntry<T> {
    val uniqueId: T
    val metadata: String
}