package bsw.webfluxsampler.reactor

import mu.KotlinLogging
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration

@Component
class BackpressureSample {
    fun run() {
        Flux
            .interval(Duration.ofMillis(1L))
//            .onBackpressureError()
            .onBackpressureDrop { logger.info { "# onDropped: $it" } }
            .doOnNext { logger.info { "# doOnNext: $it" } }
            .publishOn(Schedulers.parallel())
            .subscribe(
                {
                    Thread.sleep(5L)
                    logger.info { "# onNext: $it" }
                },
                {
                    logger.error(it) { "# onError: $it" }
                }
            )
    }

    private val logger = KotlinLogging.logger { }

}
