package org.tema.app.kafka

import Context
import apiV1RequestDeserialize
import apiV1ResponseSerialize
import fromTransport
import org.tema.kotlin.resource.statuses.api.v1.models.IRequest
import org.tema.kotlin.resource.statuses.api.v1.models.IResponse
import toTransportResource

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: Context): String {
        val response: IResponse = source.toTransportResource()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: Context) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}
