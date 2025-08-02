package validation

import Context
import ICorChainDsl
import models.State
import worker


fun ICorChainDsl<Context>.finishValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        validated = validating
    }
}

fun ICorChainDsl<Context>.finishFilterValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        filterValidated = filterValidating
    }
}
