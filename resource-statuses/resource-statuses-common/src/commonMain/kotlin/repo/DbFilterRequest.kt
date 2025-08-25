package repo

import models.ResourceStatus
import models.ResourceType
import models.UserId

data class DbFilterRequest(
    val name: String = "",
    val status: ResourceStatus = ResourceStatus.NONE,
    val type: ResourceType = ResourceType.NONE,
    val ownerId: UserId = UserId.NONE
)
