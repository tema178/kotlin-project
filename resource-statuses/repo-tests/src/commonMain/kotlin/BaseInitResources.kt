import models.*

abstract class BaseInitResources(private val op: String): IInitObjects<Resource> {
    fun createInitTestModel(
        suf: String,
        ownerId: UserId = UserId("owner-123"),
    ) = Resource(
        id = ResourceId("ad-repo-$op-$suf"),
        type = ResourceType("$suf stub type"),
        status = ResourceStatus("$suf stub status"),
        updatedBy = ownerId
    )
}
