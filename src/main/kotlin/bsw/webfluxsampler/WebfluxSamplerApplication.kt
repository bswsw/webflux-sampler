package bsw.webfluxsampler

import bsw.webfluxsampler.reactor.BackpressureSample
import bsw.webfluxsampler.reactor.FluxSample
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebfluxSamplerApplication

fun main(args: Array<String>) {
    val ctx = runApplication<WebfluxSamplerApplication>(*args)

//    val fluxSample = ctx.getBean(FluxSample::class.java)
//    fluxSample.run()

    val backpressureSample = ctx.getBean(BackpressureSample::class.java)
    backpressureSample.run()

//    val pubsub = ctx.getBean(SamplePubsub::class.java)
//    pubsub.consume()
}
