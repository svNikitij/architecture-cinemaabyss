package ru.svn.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.svn.dto.ErrorResponse

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun onValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val msg = ex.bindingResult.allErrors
            .filterIsInstance<FieldError>()
            .joinToString("; ") { "${it.field}: ${it.defaultMessage}" }
            .ifBlank { "Bad Request" }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(msg))
    }

    @ExceptionHandler(Exception::class)
    fun onAny(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(ex.message ?: "Internal Server Error"))
    }
}