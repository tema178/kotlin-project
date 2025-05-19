package models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


data class Resource(
    val id: ResourceId = ResourceId.NONE,
    val type: ResourceType = ResourceType.DEFAULT,
    val status: ResourceStatus = ResourceStatus.NONE,
    val updatedAt: Instant = Clock.System.now(),
    val updatedBy: UserId = UserId.NONE,
    val lock: Lock = Lock.NONE
)