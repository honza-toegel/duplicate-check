package org.jto.dupcheck

import org.jto.dupcheck.service.DuplicateCheckRequest
import org.jto.dupcheck.service.DuplicateCheckService
import org.jto.dupcheck.service.noDuplicateFound
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DuplicateCheckServiceTest {
    @Test
    fun checkDuplicate_whenEmpty_thenReturnFalse() {
        val cd = DuplicateCheckService<String>()
        Assertions.assertEquals(noDuplicateFound<String>(), cd.check(DuplicateCheckRequest("xxx")))
    }

    @Test
    fun checkDuplicate_whenAlreadyExist_thenReturnTrue() {
        val cd = DuplicateCheckService<String>()
        val firstEntry = DuplicateCheckRequest("xxx")
        cd.check(firstEntry)
        val response = cd.check(DuplicateCheckRequest("xxx"))
        Assertions.assertEquals(true, response.alreadyUsed)
        Assertions.assertNotNull(response.pastUsage)
        with (requireNotNull(response.pastUsage)) {
            Assertions.assertEquals( "xxx", uniqueId)
        }
    }

    @Test
    fun checkDuplicate_whenNotExist_thenReturnFalse() {
        val cd = DuplicateCheckService<String>()
        val firstEntry = DuplicateCheckRequest("xxx")
        val response = cd.check(firstEntry)
        Assertions.assertFalse(response.alreadyUsed)
        Assertions.assertNull(response.pastUsage)
    }
}