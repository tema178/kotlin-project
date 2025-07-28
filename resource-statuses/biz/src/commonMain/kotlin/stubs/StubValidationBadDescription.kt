package stubs

import Context
import ICorChainDsl
import models.ResourceError
import models.State
import util.fail
import worker

fun ICorChainDsl<Context>.stubValidationBadType(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для типа ресурса
    """.trimIndent()
    on { stubCase == Stubs.BAD_TYPE && state == State.RUNNING }
    handle {
        fail(
            ResourceError(
                group = "validation",
                code = "validation-type",
                field = "type",
                message = "Wrong type field"
            )
        )
    }
}
