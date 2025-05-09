import models.*
import org.tema.kotlin.resource.statuses.api.v1.models.*
import java.math.BigDecimal

fun Context.toTransportResource(): IResponse = when (val cmd = command) {
    Command.CREATE -> toTransportCreate()
    Command.READ -> toTransportRead()
    Command.UPDATE -> toTransportUpdate()
    Command.DELETE -> toTransportDelete()
    Command.SEARCH -> toTransportSearch()
    Command.NONE -> throw UnknownCommand(cmd)
}

fun Context.toTransportCreate() = ResourceCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    resource = resource.toTransportResource()
)

fun Context.toTransportRead() = ResourceReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    resource = resource.toTransportResource()
)

fun Context.toTransportUpdate() = ResourceUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    resource = resource.toTransportResource()
)

fun Context.toTransportDelete() = ResourceDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    resource = resource.toTransportResource()
)

fun Context.toTransportSearch() = ResourceSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    resources = resources.toTransportResource()
)

fun List<Resource>.toTransportResource(): List<ResourceResponseObject>? = this
    .map { it.toTransportResource() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Resource.toTransportResource(): ResourceResponseObject = ResourceResponseObject(
    id = id.takeIf { it != ResourceId.NONE }?.asString(),
    resourceType = type.asString(),
    status = status.asString(),
    updatedBy = updatedBy.asString(),
    updatedAt = BigDecimal.valueOf(updatedAt.epochSeconds)
)


private fun List<ResourceError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportResource() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ResourceError.toTransportResource() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun State.toResult(): ResponseResult? = when (this) {
    State.RUNNING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.FINISHING -> ResponseResult.SUCCESS
    State.NONE -> null
}
