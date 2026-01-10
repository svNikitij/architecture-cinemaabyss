package ru.svn.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import ru.svn.dto.EventEnvelope
import java.util.concurrent.TimeUnit

@Service
class EventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    @Value("\${app.kafka.topic}") private val topic: String
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun publish(event: EventEnvelope): SendResult<String, String> {
        val value = objectMapper.writeValueAsString(event)

        log.info("Producing event id={} type={} topic={}", event.id, event.type, topic)

        val future = kafkaTemplate.send(topic, event.id, value)

        return future.get(5, TimeUnit.SECONDS)
    }
}