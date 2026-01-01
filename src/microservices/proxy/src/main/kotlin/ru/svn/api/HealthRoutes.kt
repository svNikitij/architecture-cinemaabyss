package ru.svn.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class HealthRoutes {

    @Bean
    fun healthRoute() = router {
        GET("/health") {
            ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .bodyValue(mapOf("status" to "ok"))
        }
    }
}