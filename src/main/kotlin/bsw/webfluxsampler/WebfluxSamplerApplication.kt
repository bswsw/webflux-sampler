package bsw.webfluxsampler

import bsw.webfluxsampler.sample.FluxSample
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebfluxSamplerApplication

fun main(args: Array<String>) {
    val ctx = runApplication<WebfluxSamplerApplication>(*args)

    val fluxSample = ctx.getBean(FluxSample::class.java)
    fluxSample.subscribe()

//    val pubsub = ctx.getBean(SamplePubsub::class.java)
//    pubsub.consume()
}
