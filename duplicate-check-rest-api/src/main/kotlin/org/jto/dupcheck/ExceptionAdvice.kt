package org.jto.dupcheck

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionAdvice {
    @ExceptionHandler( value =  [IllegalAccessException::class, AccessDeniedException::class, Exception::class])
    fun exceptionHandler(ex: Exception): ResponseEntity<ErrorMessage> {
        logger.error("Exception occur", ex)
        val status:HttpStatus = when(ex) {
            is IllegalAccessException -> HttpStatus.FORBIDDEN
            is AccessDeniedException -> HttpStatus.FORBIDDEN
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity<ErrorMessage>(
            ErrorMessage(httpStatus = status, message = ex.message ?: ex.toString(), description = ex.cause?.toString() ?: ex.toString()), status)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ExceptionAdvice::class.java)
    }
}