package validation

import Context
import ICorChainDsl
import util.errorValidation
import util.fail
import worker

fun ICorChainDsl<Context>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { validating.id.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
