package ru.svn.dto

import com.fasterxml.jackson.databind.JsonNode
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.Instant

data class ErrorResponse(
    val error: String
)

/**
 * Event из вашей спецификации:
 * id, type, timestamp, payload
 */
data class EventEnvelope(
    val id: String,
    val type: String,
    val timestamp: Instant,
    val payload: JsonNode
)

data class EventResponse(
    val status: String,
    val partition: Int,
    val offset: Long,
    val event: EventEnvelope
)

data class MovieEventRequest(
    @field:NotNull val movieId: Long?,
    @field:NotBlank val title: String?,
    @field:NotBlank val action: String?,
    val userId: Long? = null,
    val rating: Double? = null,
    val genres: List<String>? = null,
    val description: String? = null
)

data class UserEventRequest(
    @field:NotNull val userId: Long?,
    val username: String? = null,
    val email: String? = null,
    @field:NotBlank val action: String?,
    @field:NotNull val timestamp: Instant?
)

data class PaymentEventRequest(
    @field:NotNull val paymentId: Long?,
    @field:NotNull val userId: Long?,
    @field:NotNull val amount: Double?,
    @field:NotBlank val status: String?,
    @field:NotNull val timestamp: Instant?,
    val methodType: String? = null
)