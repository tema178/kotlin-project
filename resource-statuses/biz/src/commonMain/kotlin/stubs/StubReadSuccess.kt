package stubs

import Context
import CorSettings
import ICorChainDsl
import ResourceStub
import models.ResourceStatus
import models.ResourceType
import models.State
import worker


fun ICorChainDsl<Context>.stubReadSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для чтения объявления
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubOffersSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            val stub = ResourceStub.prepareResult {
                request.status.takeIf {it != ResourceStatus.NONE  }?.also { this.status = it }
                request.type.takeIf { it != ResourceType.DEFAULT }?.also { this.type = it }
            }
            resource = stub
        }
    }
}
