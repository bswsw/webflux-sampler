package bsw.webfluxsampler.operator

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.stream.Stream

fun main() {
    Mono
        .justOrEmpty<String>(null)
        .log()
        .subscribe()

    println("============================================")

    Flux
        .fromIterable(listOf(1, 2, 3))
        .log()
        .subscribe()

    println("============================================")

    Flux
        .fromStream { Stream.of(1, 2, 3) }
        .log()
        .subscribe()

    println("============================================")

    Flux
        .range(2, 10)
        .log()
        .subscribe()

    println("============================================")

    val just = Mono.just(LocalDateTime.now())
    val defer = Mono.defer { Mono.just(LocalDateTime.now()) }.log()

    Thread.sleep(2000)

    just.log().subscribe()

    println("============================================")

    defer.log().subscribe()
}
