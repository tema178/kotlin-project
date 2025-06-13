package io.tema.app.spring.config

import io.tema.app.spring.util.StatusProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Suppress("unused")
@Configuration
class Config {
    @Bean
    fun processor() = StatusProcessor()

    @Bean
    fun appSettings(
        processor: StatusProcessor,
    ) = StatusAppSettings(
        processor = processor,
    )
}
