package models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


data class Resource(
    var id: ResourceId = ResourceId.NONE,
    var type: ResourceType = ResourceType.DEFAULT,
    var status: ResourceStatus = ResourceStatus.NONE,
    val updatedAt: Instant = Clock.System.now(),
    val updatedBy: UserId = UserId.NONE,
    val lock: Lock = Lock.NONE
)