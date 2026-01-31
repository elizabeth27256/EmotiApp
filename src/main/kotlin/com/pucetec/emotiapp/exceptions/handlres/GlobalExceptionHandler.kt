package com.pucetec.emotiapp.exceptions

import com.pucetec.emotiapp.models.responses.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    // NOT FOUND (404)

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        ex: UserNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Not Found",
            message = ex.message ?: "User not found",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(EmotionNotFoundException::class)
    fun handleEmotionNotFoundException(
        ex: EmotionNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Not Found",
            message = ex.message ?: "Emotion not found",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(RecommendationNotFoundException::class)
    fun handleRecommendationNotFoundException(
        ex: RecommendationNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Not Found",
            message = ex.message ?: "Recommendation not found",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(EmotionSelectionNotFoundException::class)
    fun handleEmotionSelectionNotFoundException(
        ex: EmotionSelectionNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = "Not Found",
            message = ex.message ?: "Emotion selection not found",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    // BAD REQUEST (400)
    @ExceptionHandler(InvalidUserException::class)
    fun handleInvalidUserException(
        ex: InvalidUserException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid user data",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidRecommendationException::class)
    fun handleInvalidRecommendationException(
        ex: InvalidRecommendationException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = ex.message ?: "Invalid recommendation data",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    // CONFLICT (409)

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(
        ex: UserAlreadyExistsException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            message = ex.message ?: "User already exists",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    // INTERNAL SERVER ERROR (500)

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = ex.message ?: "An unexpected error occurred",
            path = request.getDescription(false).replace("uri=", "")
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

// ERROR RESPONSE DTO

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String? = null
)