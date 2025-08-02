package validation

import Context
import ICorChainDsl
import util.errorValidation
import util.fail
import worker


fun ICorChainDsl<Context>.validateStatusNotEmpty(title: String) = worker {
    this.title = title
    on { validating.status.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "status",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
