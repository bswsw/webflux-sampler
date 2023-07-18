package bsw.webfluxsampler.step

import mu.KotlinLogging
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * Step1. 퍼블리셔와 서브스크라이버를 직접 구현 한다.
 */
fun main() {
    val logger = KotlinLogging.logger {}

//    val pub = object : Publisher<Int> {
//        override fun subscribe(sub: Subscriber<in Int>) {
//            logger.info { "subscribe" }
//
//            sub.onSubscribe(object : Subscription {
//                override fun request(n: Long) {
//                    logger.info { "request: $n" }
//
//                    (1..n.toInt()).forEach {
//                        sub.onNext(it)
//                    }
//                    sub.onComplete()
//                }
//
//                override fun cancel() {
//                    TODO("Not yet implemented")
//                }
//            })
//        }
//    }

    val pub = Publisher { sub ->
        logger.info { "subscribe" }

        sub.onSubscribe(object : Subscription {
            override fun request(n: Long) {
                logger.info { "request: $n" }

                (1..10).forEach {
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
            logger.info { "onSubscribe" }
            s.request(0)
        }

        override fun onNext(t: Int) {
            logger.info { "onNext: $t" }
        }

        override fun onError(t: Throwable) {
            logger.error(t) { "onError" }
        }

        override fun onComplete() {
            logger.info { "onComplete" }
        }
    }

    pub.subscribe(sub)
}