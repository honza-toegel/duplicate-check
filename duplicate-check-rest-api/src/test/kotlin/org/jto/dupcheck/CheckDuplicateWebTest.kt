package org.jto.dupcheck

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.Matchers
import org.jto.dupcheck.service.DuplicateCheckRequest
import org.jto.dupcheck.service.DuplicateCheckService
import org.jto.dupcheck.service.StringDuplicateCheckRest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(StringDuplicateCheckRest::class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
class CheckDuplicateWebTest(
    @Autowired private val mvc: MockMvc,
    @Autowired private val duplicateCheckService: DuplicateCheckService<String>
) {
    @BeforeEach
    fun clear() {
        duplicateCheckService.houseKeeping(0)
    }

    @Test
    fun checkOnEmpty_thenReturnNoDuplicate() {
        val entry = DuplicateCheckRequest("test")
        mvc.post("/") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(entry)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("{\"alreadyUsed\":false,\"pastUsage\":null}") }
        }.andDo {
            document("{methodName}")
        }
    }

    @Test
    fun checkTwiceSame_thenReturnDuplicate() {
        val entry = DuplicateCheckRequest("test", "meta1")
        mvc.post("/") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(entry)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("{\"alreadyUsed\":false,\"pastUsage\":null}") }
        }.andDo {
            document("{methodName}-first")
        }

        val entry2 = DuplicateCheckRequest("test", "meta2-different-but-same:)")
        mvc.post("/") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(entry2)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { jsonPath("$.alreadyUsed", Matchers.`is`(true)) }
            content { jsonPath("$.pastUsage.uniqueId", Matchers.`is`(entry.uniqueId)) }
        }.andDo {
            document("{methodName}-second")
        }
    }

    @Test
    fun checkTwiceDifferent_thenReturnNoDuplicate() {
        val entry = DuplicateCheckRequest("test")
        mvc.post("/") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(entry)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("{\"alreadyUsed\":false,\"pastUsage\":null}") }
        }.andDo {
            document("{methodName}-first")
        }

        val entry2 = DuplicateCheckRequest("test2")
        mvc.post("/") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(entry2)
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json("{\"alreadyUsed\":false,\"pastUsage\":null}") }
        }.andDo {
            document("{methodName}-second")
        }
    }
}