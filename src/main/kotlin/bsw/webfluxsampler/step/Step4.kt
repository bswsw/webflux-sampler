package bsw.webfluxsampler.step

import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * Step 4. 오퍼레이터 체이닝 하기
 */
fun <T, R> Publisher<T>.map(f: (T) -> R): Publisher<R> = PublisherMap(this, f)
fun <T> Publisher<T>.filter(f: (T) -> Boolean): Publisher<T> = PublisherFilter(this, f)
fun <T, R> Publisher<T>.foldLeft(init: R, f: (R, T) -> R): Publisher<R> = PublisherFoldLeft(this, init, f)

fun main() {
    val logger = KotlinLogging.logger {}

    val pub = Publisher { sub ->
        logger.info { "pub: subscribe" }

        sub.onSubscribe(object : Subscription {
            override fun request(n: Long) {
                logger.info { "pub: request: $n" }

                (1..n.toInt()).forEach {
                    sub.onNext(it)
                }
                sub.onComplete()
            }

            override fun cancel() {
                TODO("Not yet implemented")
            }
        })
    }

    val sub = object : Subscriber<Int> {
        override fun onSubscribe(s: Subscription) {
            logger.info { "sub: onSubscribe" }
            s.request(5)
        }

        override fun onNext(t: Int) {
            logger.info { "sub: onNext: $t" }
        }

        override fun onError(t: Throwable) {
            logger.error(t) { "sub: onError" }
        }

        override fun onComplete() {
            logger.info { "sub: onComplete" }
        }
    }

    pub
        .map { it + 1 }
        .filter { it % 2 == 0 }
        .foldLeft(0) { acc, el -> acc + el }
        .subscribe(sub)
}