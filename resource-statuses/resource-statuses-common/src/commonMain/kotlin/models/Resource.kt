package models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


data class Resource(
    var id: ResourceId = ResourceId.NONE,
    var name: String = "",
    var type: ResourceType = ResourceType.DEFAULT,
    var status: ResourceStatus = ResourceStatus.NONE,
    var updatedAt: Instant = Clock.System.now(),
    var updatedBy: UserId = UserId.NONE,
    var lock: Lock = Lock.NONE
)