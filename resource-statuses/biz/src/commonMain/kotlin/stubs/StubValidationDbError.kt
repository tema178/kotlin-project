package stubs

import Context
import ICorChainDsl
import models.ResourceError
import models.State
import util.fail
import worker


fun ICorChainDsl<Context>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == Stubs.DB_ERROR && state == State.RUNNING }
    handle {
        fail(
            ResourceError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
