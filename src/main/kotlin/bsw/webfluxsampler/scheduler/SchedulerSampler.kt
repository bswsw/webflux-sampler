package bsw.webfluxsampler.scheduler

import mu.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

fun main() {
    val logger = KotlinLogging.logger { }

    println(System.getProperty("reactor.schedulers.defaultPoolSize"))
    println(Runtime.getRuntime().availableProcessors())

//    Flux
//        .range(1, 1000)
//        .delayElements(Duration.ofMillis(100))
//        .map { it + 2 }
//        .log()
//        .subscribe()

    Flux
        .range(1, 100)
        .subscribeOn(Schedulers.boundedElastic())
        .doOnNext { a -> logger.info { "doOnNext: $a" } }
        .log()
        .subscribe { a -> logger.info("onNext: $a") }

//    Mono
//        .just("asd")
//        .delayElement(Duration.ofMillis(100))
//        .log()
//        .subscribe()

//    Mono
//        .delay(Duration.ofMillis(1000))
//        .log()
//        .subscribe()

    Thread.sleep(10000)
}

/**
 * public static Flux<Long> interval(Duration period) {
 * 		return interval(period, Schedulers.parallel());
 * 	}
 *
 * 	public final Flux<T> delayElements(Duration delay) {
 * 		return delayElements(delay, Schedulers.parallel());
 * 	}
 */

