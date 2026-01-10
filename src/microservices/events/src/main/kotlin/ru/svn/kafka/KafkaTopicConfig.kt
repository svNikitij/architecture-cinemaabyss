package ru.svn.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfig(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    @Value("\${app.kafka.topic}") private val topicName: String,
    @Value("\${app.kafka.partitions:3}") private val partitions: Int,
    @Value("\${app.kafka.replicas:1}") private val replicas: Int
) {
    @Bean
    fun kafkaAdmin(): KafkaAdmin =
        KafkaAdmin(mapOf(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers))

    @Bean
    fun eventsTopic(): NewTopic =
        TopicBuilder.name(topicName)
            .partitions(partitions)
            .replicas(replicas)
            .build()
}