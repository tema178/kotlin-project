package validation

import Context
import ICorChainDsl
import models.State
import worker


fun ICorChainDsl<Context>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        validated = validating
    }
}

fun ICorChainDsl<Context>.finishAdFilterValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        filterValidated = filterValidating
    }
}
