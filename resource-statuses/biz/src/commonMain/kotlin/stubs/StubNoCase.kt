package stubs

import Context
import ICorChainDsl
import models.ResourceError
import models.State
import util.fail
import worker


fun ICorChainDsl<Context>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == State.RUNNING }
    handle {
        fail(
            ResourceError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
