package org.jto.dupcheck.service

data class DuplicateCheckRequest<T>(
    override val uniqueId: T,
    override val metadata: String = ""
) : IBaseEntry<T> {
    override fun toString(): String = "(id=$uniqueId, meta=$metadata)"
}
