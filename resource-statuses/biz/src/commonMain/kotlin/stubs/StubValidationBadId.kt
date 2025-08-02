package stubs

import Context
import ICorChainDsl
import models.ResourceError
import models.State
import util.fail
import worker


fun ICorChainDsl<Context>.stubValidationBadId(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для идентификатора объявления
    """.trimIndent()
    on { stubCase == Stubs.BAD_ID && state == State.RUNNING }
    handle {
        fail(
            ResourceError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
