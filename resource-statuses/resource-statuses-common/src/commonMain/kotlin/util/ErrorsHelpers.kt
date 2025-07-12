package util

import models.ResourceError


fun Throwable.asResourceError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = ResourceError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
