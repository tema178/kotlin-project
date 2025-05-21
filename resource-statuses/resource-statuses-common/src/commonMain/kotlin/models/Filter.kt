package models

data class Filter(
    var id: ResourceId = ResourceId.NONE,
    var type: ResourceType = ResourceType.DEFAULT,
    var status: ResourceStatus = ResourceStatus.NONE,
    var ownerId: UserId = UserId.NONE,
)
