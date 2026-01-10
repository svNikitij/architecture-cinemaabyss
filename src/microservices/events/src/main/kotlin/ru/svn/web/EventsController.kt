package ru.svn.web

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.svn.dto.EventEnvelope
import ru.svn.dto.EventResponse
import ru.svn.dto.MovieEventRequest
import ru.svn.dto.PaymentEventRequest
import ru.svn.dto.UserEventRequest
import ru.svn.service.EventPublisher
import java.time.Instant
import java.util.UUID

@RestController
@RequestMapping("/api/events")
class EventsController(
    private val publisher: EventPublisher,
    private val objectMapper: ObjectMapper
) {
    @GetMapping("/health")
    fun health(): Map<String, Boolean> = mapOf("status" to true)

    @PostMapping("/movie")
    fun createMovieEvent(@Valid @RequestBody req: MovieEventRequest): ResponseEntity<EventResponse> {
        val event = EventEnvelope(
            id = "movie-${req.movieId}-${req.action}-${UUID.randomUUID()}",
            type = "movie",
            timestamp = Instant.now(),
            payload = objectMapper.valueToTree(req)
        )

        val sendResult = publisher.publish(event)
        val meta = sendResult.recordMetadata

        return ResponseEntity.status(HttpStatus.CREATED).body(
            EventResponse(
                status = "success",
                partition = meta.partition(),
                offset = meta.offset(),
                event = event
            )
        )
    }

    @PostMapping("/user")
    fun createUserEvent(@Valid @RequestBody req: UserEventRequest): ResponseEntity<EventResponse> {
        val event = EventEnvelope(
            id = "user-${req.userId}-${req.action}-${UUID.randomUUID()}",
            type = "user",
            timestamp = Instant.now(),
            payload = objectMapper.valueToTree(req)
        )

        val sendResult = publisher.publish(event)
        val meta = sendResult.recordMetadata

        return ResponseEntity.status(HttpStatus.CREATED).body(
            EventResponse(
                status = "success",
                partition = meta.partition(),
                offset = meta.offset(),
                event = event
            )
        )
    }

    @PostMapping("/payment")
    fun createPaymentEvent(@Valid @RequestBody req: PaymentEventRequest): ResponseEntity<EventResponse> {
        val event = EventEnvelope(
            id = "payment-${req.paymentId}-${req.status}-${UUID.randomUUID()}",
            type = "payment",
            timestamp = Instant.now(),
            payload = objectMapper.valueToTree(req)
        )

        val sendResult = publisher.publish(event)
        val meta = sendResult.recordMetadata

        return ResponseEntity.status(HttpStatus.CREATED).body(
            EventResponse(
                status = "success",
                partition = meta.partition(),
                offset = meta.offset(),
                event = event
            )
        )
    }
}