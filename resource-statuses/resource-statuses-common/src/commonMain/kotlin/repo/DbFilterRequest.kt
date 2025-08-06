package repo

import models.ResourceStatus
import models.ResourceType
import models.UserId

data class DbFilterRequest(
    val status: ResourceStatus = ResourceStatus.NONE,
    val type: ResourceType = ResourceType.DEFAULT,
    val ownerId: UserId = UserId.NONE
)
