package com.emailmarketingtool.exceptions.handlers

import com.emailmarketingtool.errors.ErrorMessages
import com.emailmarketingtool.errors.ErrorResponse
import com.emailmarketingtool.exceptions.BaseException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException

@RestControllerAdvice
class BaseExceptionHandler {

    @Value("\${spring.servlet.multipart.max-file-size}")
    private lateinit var maxFileSize: String

    @ExceptionHandler(BaseException::class)
    fun handleCustomException(ex: BaseException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(ex.error.statusCode.value())
            .body(ErrorResponse(ex.error.statusCode.value(), ex.error.statusCode, ex.error.message))
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRunTimeException(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.internalServerError()
            .body(
                ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.message.orEmpty()
                )
            )
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(e: MaxUploadSizeExceededException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                ErrorMessages.FILE_SIZE_SHOULD_BE_LESS_THAN + maxFileSize
            )
        )
    }
}