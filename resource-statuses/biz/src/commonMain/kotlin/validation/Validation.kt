package validation

import Context
import ICorChainDsl
import chain
import models.State

fun ICorChainDsl<Context>.validation(block: ICorChainDsl<Context>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == State.RUNNING }
}
