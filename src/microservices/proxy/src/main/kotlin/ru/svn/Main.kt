package ru.svn

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@SpringBootApplication
@ConfigurationPropertiesScan
class ProxyServiceApplication

fun main(args: Array<String>) {
    try {
        SpringApplication.run(ProxyServiceApplication::class.java, *args)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}