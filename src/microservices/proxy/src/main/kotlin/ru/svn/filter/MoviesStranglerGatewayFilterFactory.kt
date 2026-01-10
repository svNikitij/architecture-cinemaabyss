package ru.svn.filter

import ru.svn.config.StranglerProperties
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.ThreadLocalRandom

@Service
class MoviesStranglerGatewayFilterFactory(
    private val props: StranglerProperties
) : AbstractGatewayFilterFactory<MoviesStranglerGatewayFilterFactory.Config>(Config::class.java) {

    private val log = LoggerFactory.getLogger(javaClass)

    class Config

    override fun apply(config: Config): GatewayFilter {
        val filter = GatewayFilter { exchange, chain ->
            val percent = props.moviesMigrationPercent.coerceIn(0.0, 100.0)

            val random = ThreadLocalRandom.current().nextDouble(0.0, 100.0)
            val useNew = if (!props.gradualMigration) {
                true
            } else {
                random < percent
            }

            val targetBase = if (useNew) props.moviesServiceUrl else props.monolithUrl
            val incoming = exchange.request.uri

            val newUrl = UriComponentsBuilder.fromUri(targetBase)
                .path(incoming.rawPath)
                .query(incoming.rawQuery)
                .build(true)
                .toUri()

            val targetLabel = if (useNew) "NEW(movies-service)" else "OLD(monolith)"
            log.info(
                "Movies route: path={}, gradual={}, percent={}, random={}, target={}, targetBase={}",
                incoming.rawPath, props.gradualMigration, percent, String.format("%.2f", random), targetLabel, targetBase
            )

            exchange.attributes[GATEWAY_REQUEST_URL_ATTR] = newUrl

            val mutated = exchange.mutate()
                .request { it.headers { h -> h.add("X-Strangler-Target", targetLabel) } }
                .build()

            chain.filter(mutated)
        }

        //TODO придумать как решить более оптимальным способом
        // небольшой костыль для работы с фильтрвми spring cloud
        // Order 10001 — выполнится после стандартного RouteToRequestUrlFilter (10000)
        return OrderedGatewayFilter(filter, 10001)
    }
}