import kotlinx.datetime.Clock
import models.*
import org.tema.kotlin.resource.statuses.api.log.models.*

fun Context.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "resource-statuses",
    statuses = toResourceStatusLog(),
    errors = errors.map { it.toLog() },
)

private fun Context.toResourceStatusLog(): ResourceStatusesLogModel? {
    val adNone = Resource()
    return ResourceStatusesLogModel(
        requestId = requestId.takeIf { it != RequestId.NONE }?.asString(),
        request = request.takeIf { it != adNone }?.toLog(),
        response = resource.takeIf { it != adNone }?.toLog(),
        responses = resources.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = filterRequest.takeIf { it != Filter() }?.toLog(),
    ).takeIf { it != ResourceStatusesLogModel() }
}

private fun Filter.toLog() = FilterLog(
    id = id.takeIf { it != ResourceId.NONE }?.asString(),
    resourceType = type.asString(),
    status = status.takeIf { it != ResourceStatus.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
)

private fun ResourceError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun Resource.toLog() = Log(
    id = id.takeIf { it != ResourceId.NONE }?.asString(),
    resourceType = type.takeIf { it != ResourceType.DEFAULT}?.asString(),
    status = status.takeIf { it != ResourceStatus.NONE }?.asString(),
    updatedBy = updatedBy.takeIf { it != UserId.NONE }?.asString(),
    updatedAt = updatedAt.toString()
)
