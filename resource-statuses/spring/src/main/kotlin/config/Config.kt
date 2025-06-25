package org.tema.app.spring.config

import CorSettings
import LoggerProvider
import StatusProcessor
import loggerLogback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
class Config {
    @Bean
    fun processor(corSettings: CorSettings) = StatusProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { loggerLogback(it) }

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider()
    )

    @Bean
    fun appSettings(
        processor: StatusProcessor,
        corSettings: CorSettings
    ) = StatusAppSettings(
        processor = processor,
        corSettings = corSettings
    )
}
