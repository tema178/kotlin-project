package org.tema.app.kafka

import Context

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: Context): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: Context)
}
