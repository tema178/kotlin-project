package validation

import Context
import ICorChainDsl
import util.errorValidation
import util.fail
import worker


fun ICorChainDsl<Context>.validateTypeNotEmpty(title: String) = worker {
    this.title = title
    on { validating.type.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "type",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
