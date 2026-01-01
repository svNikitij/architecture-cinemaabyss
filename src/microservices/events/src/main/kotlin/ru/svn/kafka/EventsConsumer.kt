package ru.svn.kafka

import ru.svn.dto.EventEnvelope
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class EventsConsumer(
    private val objectMapper: ObjectMapper,
    @Value("\${app.kafka.topic}") private val topic: String
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${app.kafka.topic}"])
    fun onMessage(record: ConsumerRecord<String, String>) {
        val key = record.key()
        val value = record.value()

        val event = runCatching { objectMapper.readValue(value, EventEnvelope::class.java) }
            .getOrElse { ex ->
                log.error(
                    "Failed to parse event from topic={} partition={} offset={} key={}. Raw={}",
                    topic, record.partition(), record.offset(), key, value, ex
                )
                return
            }

        log.info(
            "Consumed event: id={} type={} ts={} topic={} partition={} offset={} key={} payload={}",
            event.id, event.type, event.timestamp,
            topic, record.partition(), record.offset(), key,
            event.payload.toString()
        )
    }
}