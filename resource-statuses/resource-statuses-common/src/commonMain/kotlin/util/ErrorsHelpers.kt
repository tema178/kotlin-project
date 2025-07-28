package util

import Context
import LogLevel
import models.ResourceError
import models.State


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

inline fun Context.addError(vararg error: ResourceError) = errors.addAll(error)

inline fun Context.fail(error: ResourceError) {
    addError(error)
    state = State.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = ResourceError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)