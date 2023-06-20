package bsw.webfluxsampler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebfluxSamplerApplication

fun main(args: Array<String>) {
    runApplication<WebfluxSamplerApplication>(*args)
}
