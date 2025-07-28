package general

import Context
import ICorChainDsl
import models.State
import worker

fun ICorChainDsl<Context>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == State.NONE }
    handle { state = State.RUNNING }
}
