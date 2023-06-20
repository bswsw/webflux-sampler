package bsw.webfluxsampler.sample

import reactor.core.publisher.Flux

fun main(args: Array<String>) {
    Flux
        .just("hello", "world")
        .map { it.uppercase() }
        .subscribe { println(it) }
}