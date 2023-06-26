package bsw.webfluxsampler.sample

import mu.KotlinLogging
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration

@Component
class FluxSample {
    fun subscribe() {
        val flux = Flux
            .range(1, 1000)
            .delayElements(Duration.ofMillis(100))
            .log()
            .share()


        flux.subscribe {
//            logger.info { "[subscriber1] $it" }
        }

        Thread.sleep(2000)

        flux.subscribe {
//            logger.info { "[subscriber2] $it" }
        }
    }

    private val logger = KotlinLogging.logger { }
}