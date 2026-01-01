package ru.svn

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@SpringBootApplication
@ConfigurationPropertiesScan
class EventsServiceApplication

fun main(args: Array<String>) {
    try {
        SpringApplication.run(EventsServiceApplication::class.java, *args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}