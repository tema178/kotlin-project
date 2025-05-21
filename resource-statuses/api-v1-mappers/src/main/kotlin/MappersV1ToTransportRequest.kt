import models.Resource
import org.tema.kotlin.resource.statuses.api.v1.models.*

fun Resource.toTransportCreateResource() = ResourceCreateObject(
    id = id.asString(),
    resourceType = type.asString(),
    status = status.asString()
    )

fun Resource.toTransportReadResource() = ResourceReadObject(
    id = id.asString(),
    resourceType = type.asString()
)

fun Resource.toTransportUpdateResource() = ResourceUpdateObject(
    id = id.asString(),
    resourceType = type.asString(),
    status = status.asString(),
    lock = lock.asString(),
)

fun Resource.toTransportDeleteResource() = ResourceDeleteObject(
    id = id.asString(),
    resourceType = type.asString(),
    lock = lock.asString(),
)

fun Resource.toTransportSearchResource() = ResourceSearchFilter(
    id = id.asString(),
    resourceType = type.asString(),
    status = status.asString(),
    updatedBy = updatedBy.asString()
)

