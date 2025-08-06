package org.tema.app.spring.config

import CorSettings
import LoggerProvider
import RepoInMemory
import StatusProcessor
import loggerLogback
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import repo.IRepo
import repo.RepoBase

@Suppress("unused")
@Configuration
class Config {
    @Bean
    fun processor(corSettings: CorSettings) = StatusProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { loggerLogback(it) }

    @Bean
    fun testRepo(): IRepo = RepoInMemory()

    @Bean
    fun prodRepo(): IRepo = RepoInMemory()

    @Bean
    fun stubRepo(): IRepo = RepoInMemory()

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
        repoProd = prodRepo(),
        repoTest = testRepo(),
        repoStub = stubRepo()
    )

    @Bean
    fun appSettings(
        processor: StatusProcessor,
        corSettings: CorSettings
    ) = StatusAppSettings(
        processor = processor,
        corSettings = corSettings,
    )
}
