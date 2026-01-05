package ru.svn.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.net.URI

@Configuration
@ConfigurationProperties(prefix = "cinemaabyss.strangler")
data class StranglerProperties(
    var gradualMigration: Boolean = true,
    var moviesMigrationPercent: Double = 0.0,
    var monolithUrl: URI = URI.create("http://localhost:8080"),
    var moviesServiceUrl: URI = URI.create("http://localhost:8081"),
)