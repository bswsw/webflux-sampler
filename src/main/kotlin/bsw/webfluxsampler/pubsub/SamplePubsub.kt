package bsw.webfluxsampler.pubsub

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.stereotype.Component
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions

@Component
class SamplePubsub {

    private val properties =
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
            ConsumerConfig.GROUP_ID_CONFIG to "test-webflux-group",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
        )

    private val receiverOptions =
        ReceiverOptions
            .create<String, Any>(properties)
            .subscription(listOf("test-topic"))
            .addAssignListener { println("onPartitionAssigned: $it") }
            .addRevokeListener { println("onPartitionRevoked: $it") }

    fun consume() {
        KafkaReceiver
            .create(receiverOptions)
            .receive()
            .subscribe { record ->
                val partition = record.partition()
                val offset = record.receiverOffset()
                val key = record.key()
                val value = record.value()

                logger.info { "[partition=${partition}, offset=${offset.offset()}, key=${key}] $value" }

                offset.acknowledge()
            }
    }

    private val logger = KotlinLogging.logger { }
}