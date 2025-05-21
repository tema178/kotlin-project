import exceptions.UnknownRequestClass
import models.*
import org.tema.kotlin.resource.statuses.api.v1.models.*
import stubs.Stubs

fun Context.fromTransport(request: IRequest) = when (request) {
    is ResourceCreateRequest -> fromTransport(request)
    is ResourceReadRequest -> fromTransport(request)
    is ResourceUpdateRequest -> fromTransport(request)
    is ResourceDeleteRequest -> fromTransport(request)
    is ResourceSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toResourceId() = this?.let { ResourceId(it) } ?: ResourceId.NONE
private fun String?.toResourceLock() = this?.let { Lock(it) } ?: Lock.NONE

private fun Debug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    RequestDebugMode.PROD -> WorkMode.PROD
    RequestDebugMode.TEST -> WorkMode.TEST
    RequestDebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun Debug?.transportToStubCase(): Stubs = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    RequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    RequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    RequestDebugStubs.BAD_TYPE -> Stubs.BAD_TYPE
    RequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    RequestDebugStubs.BAD_SEARCH_STRING -> Stubs.BAD_SEARCH_STRING
    null -> Stubs.NONE
}

fun Context.fromTransport(resourceRequest: ResourceCreateRequest) {
    command = Command.CREATE
    request = resourceRequest.resource?.toInternal() ?: Resource()
    workMode = resourceRequest.debug.transportToWorkMode()
    stubCase = resourceRequest.debug.transportToStubCase()
}

fun Context.fromTransport(request: ResourceReadRequest) {
    command = Command.READ
    this.request = request.resource.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ResourceReadObject?.toInternal(): Resource = if (this != null) {
    Resource(id = id.toResourceId())
} else {
    Resource()
}


fun Context.fromTransport(request: ResourceUpdateRequest) {
    command = Command.UPDATE
    this.request = request.resource?.toInternal() ?: Resource()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ResourceDeleteRequest) {
    command = Command.DELETE
    this.request = request.resource.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ResourceDeleteObject?.toInternal(): Resource = if (this != null) {
    Resource(
        id = id.toResourceId(),
        lock = lock.toResourceLock(),
    )
} else {
    Resource()
}

fun Context.fromTransport(request: ResourceSearchRequest) {
    command = Command.SEARCH
    filterRequest = request.resourceFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ResourceSearchFilter?.toInternal(): Filter = Filter(
    id = this?.id?.let(::ResourceId) ?: ResourceId.NONE,
    type = this?.resourceType?.let(::ResourceType) ?: ResourceType.DEFAULT,
    status = this?.status?.let(::ResourceStatus) ?: ResourceStatus.NONE,
    ownerId = this?.updatedBy?.let (::UserId) ?: UserId.NONE,
)

private fun ResourceCreateObject.toInternal(): Resource = Resource(
    id = this.id?.let(::ResourceId) ?: ResourceId.NONE,
    type = this.resourceType?.let(::ResourceType) ?: ResourceType.DEFAULT,
    status = this.status?.let(::ResourceStatus) ?: ResourceStatus.NONE
)

private fun ResourceUpdateObject.toInternal(): Resource = Resource(
    id = this.id?.let(::ResourceId) ?: ResourceId.NONE,
    type = this.resourceType?.let(::ResourceType) ?: ResourceType.DEFAULT,
    status = this.status?.let(::ResourceStatus) ?: ResourceStatus.NONE,
    lock = lock.toResourceLock(),
)


