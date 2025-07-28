package stubs

import Context
import CorSettings
import ICorChainDsl
import ResourceStub
import models.State
import worker

fun ICorChainDsl<Context>.stubSearchSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска ресурса
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            resources.addAll(ResourceStub.prepareSearchList(filterRequest.type))
        }
    }
}
