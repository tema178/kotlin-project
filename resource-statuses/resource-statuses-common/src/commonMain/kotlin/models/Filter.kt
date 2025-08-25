package models

data class Filter(
    var id: ResourceId = ResourceId.NONE,
    var name: String = "",
    var type: ResourceType = ResourceType.DEFAULT,
    var status: ResourceStatus = ResourceStatus.NONE,
    var ownerId: UserId = UserId.NONE,
)
