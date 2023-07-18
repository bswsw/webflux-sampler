package bsw.webfluxsampler.step

import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * Step 2. map, filter, fold 등 연산자를 간단히 구현해본다.
 */
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

    fun <T, R> map(pub: Publisher<T>, f: (T) -> R): Publisher<R> = Publisher { mapSub ->
        logger.info { "mapPub: subscribe" }

        pub.subscribe(object : Subscriber<T> {
            override fun onSubscribe(s: Subscription) {
                logger.info { "mapSub: onSubscribe" }
                mapSub.onSubscribe(s)
            }

            override fun onNext(t: T) {
                logger.info { "mapSub: onNext: $t" }
                mapSub.onNext(f(t))
            }

            override fun onError(t: Throwable) {
                logger.error(t) { "mapSub: onError" }
                mapSub.onError(t)
            }

            override fun onComplete() {
                logger.info { "mapSub: onComplete" }
                mapSub.onComplete()
            }
        })
    }

    fun <T> filter(pub: Publisher<T>, f: (T) -> Boolean): Publisher<T> = object : Publisher<T> {
        override fun subscribe(filterSub: Subscriber<in T>) {
            logger.info { "filterPub: subscribe" }

            pub.subscribe(object : Subscriber<T> {
                override fun onSubscribe(s: Subscription) {
                    logger.info { "filterSub: onSubscribe" }
                    filterSub.onSubscribe(s)
                }

                override fun onNext(t: T) {
                    logger.info { "filterSub: onNext: $t" }

                    if (f(t)) {
                        filterSub.onNext(t)
                    }
                }

                override fun onError(t: Throwable) {
                    logger.error(t) { "filterSub: onError" }
                    filterSub.onError(t)
                }

                override fun onComplete() {
                    logger.info { "filterSub: onComplete" }
                    filterSub.onComplete()
                }
            })
        }
    }

    fun <T, R> foldLeft(pub: Publisher<T>, init: R, f: (R, T) -> R): Publisher<R> = Publisher { foldLeftSub ->
        logger.info { "foldLeftPub: subscribe" }

        pub.subscribe(object : Subscriber<T> {
            var acc = init

            override fun onSubscribe(s: Subscription) {
                logger.info { "foldLeftSub: onSubscribe" }
                foldLeftSub.onSubscribe(s)
            }

            override fun onNext(t: T) {
                logger.info { "foldLeftSub: onNext: $t" }
                acc = f(acc, t)
            }

            override fun onError(t: Throwable) {
                logger.error(t) { "foldLeftSub: onError" }
                foldLeftSub.onError(t)
            }

            override fun onComplete() {
                logger.info { "foldLeftSub: onComplete" }
                foldLeftSub.onNext(acc)
                foldLeftSub.onComplete()
            }
        })
    }

    // pub -> map -> filter -> sub
    val mapped = map(pub) { it + 1 }
    val filtered = filter(mapped) { it % 2 == 0 }
    val folded = foldLeft(filtered, 0) { acc, el -> acc + el }

    folded.subscribe(sub)

//    foldLeft(filter(map(pub, { it * 2 }), { it > 0 }), 0, { acc, el -> acc + el })
//        .subscribe(sub)

//    foldLeft(pub, 0) { acc, el -> acc + el }.subscribe(sub)
//    foldLeft(pub, 0) { acc, el -> acc + el }.subscribe(sub)
}