package org.tema.app.spring.config

import CorSettings
import LoggerProvider
import RepoInMemory
import RepoSql
import StatusProcessor
import loggerLogback
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import repo.IRepo

@Suppress("unused")
@EnableConfigurationProperties(ConfigPostgres::class)
@Configuration
class Config (val postgresConfig: ConfigPostgres) {

    val logger: Logger = LoggerFactory.getLogger(Config::class.java)

    @Bean
    fun processor(corSettings: CorSettings) = StatusProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { loggerLogback(it) }

    @Bean
    fun testRepo(): IRepo = RepoInMemory()

    @Bean
    fun prodRepo(): IRepo = RepoSql(postgresConfig.psql).apply {
        logger.info("Connecting to DB with ${this}")
    }

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
