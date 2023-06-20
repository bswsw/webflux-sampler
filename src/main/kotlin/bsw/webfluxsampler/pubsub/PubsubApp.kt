package bsw.webfluxsampler.pubsub

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.receiver.ReceiverOptions


fun main(args: Array<String>) {
    println("pubsub start...")

    val properties = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to listOf("localhost:9092"),
        ConsumerConfig.GROUP_ID_CONFIG to "test-webflux-group",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java
    )

    val receiverOptions: ReceiverOptions<String, Any> = ReceiverOptions
        .create<String, Any>(properties)
        .subscription(listOf("test-topic"))
        .addAssignListener { println("onPartitionAssigned: $it") }
        .addRevokeListener { println("onPartitionRevoked: $it") }

    KafkaReceiver
        .create(receiverOptions)
        .receive()
        .subscribe { record ->
            val partition = record.partition()
            val offset = record.receiverOffset()
            val key = record.key()
            val value = record.value()

            println(
                """
                partition = $partition
                offset = ${offset.offset()}
                key = $key
                value = $value
            """.trimIndent()
            )

            offset.acknowledge()
        }
}