package repo

import models.Lock
import models.Resource
import models.ResourceError
import models.ResourceId
import repo.exceptions.RepoConcurrencyException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: ResourceId) = DbResponseErr(
    ResourceError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbResponseErr(
    ResourceError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldResource: Resource,
    expectedLock: Lock,
    exception: Exception = RepoConcurrencyException(
        id = oldResource.id,
        expectedLock = expectedLock,
        actualLock = oldResource.lock,
    ),
) = DbResponseErrWithData(
    ad = oldResource,
    err = ResourceError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldResource.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)
