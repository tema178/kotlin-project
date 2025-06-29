package org.tema.app.kafka

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import org.tema.kotlin.resource.statuses.api.v1.models.*
import java.util.*
import kotlin.test.assertEquals


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        ResourceCreateRequest(
                            resource = ResourceCreateObject(
                                id = "PeaceAndWar",
                                resourceType = "book",
                                status = "available"
                            ),
                            debug = Debug(
                                mode = RequestDebugMode.STUB,
                                stub = RequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<ResourceCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("PeaceAndWar", result.resource?.id)
    }

    companion object {
        const val PARTITION = 0
    }
}


