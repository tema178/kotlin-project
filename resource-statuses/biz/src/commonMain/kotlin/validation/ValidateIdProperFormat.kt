package validation

import Context
import ICorChainDsl
import models.ResourceId
import util.errorValidation
import util.fail
import worker


fun ICorChainDsl<Context>.validateIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { validating.id != ResourceId.NONE && !validating.id.asString().matches(regExp) }
    handle {
        val encodedId = validating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
