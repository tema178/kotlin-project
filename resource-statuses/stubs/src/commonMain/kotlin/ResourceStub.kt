import ResourceStubBook.RESOURCE_BOOK_AVAILABLE
import kotlinx.datetime.Instant
import models.Resource
import models.ResourceId
import models.ResourceType

object ResourceStub {

    fun get(): Resource = RESOURCE_BOOK_AVAILABLE.copy()

    fun prepareResult(block: Resource.() -> Unit): Resource = get().apply(block)

    fun getList(): List<Resource> = listOf(get(), get())


    private fun resourceSupply(id: String, type: ResourceType) =
        resource(RESOURCE_BOOK_AVAILABLE, id = id, type = type)

    private fun resourceSupply(id: String, type: ResourceType, updatedAt: Instant) =
        resource(RESOURCE_BOOK_AVAILABLE, id = id, type = type, updatedAt = updatedAt)

    private fun resource(base: Resource, id: String, type: ResourceType) = base.copy(
        id = ResourceId(id),
        type = type,
    )

    private fun resource(base: Resource, id: String, type: ResourceType, updatedAt: Instant) = base.copy(
        id = ResourceId(id),
        type = type,
        updatedAt = updatedAt
    )

    fun prepareSearchList(type: ResourceType): Collection<Resource> {
        return listOf(
            resourceSupply("1", type = type),
            resourceSupply("2", type = type),
            resourceSupply("3", type = type),
            resourceSupply("4", type = type),
            resourceSupply("5", type = type),
            resourceSupply("6", type = type),
        )
    }

    fun prepareSearchList(type: ResourceType, updatedAt: Instant): Collection<Resource> {
        return listOf(
            resourceSupply("1", type = type, updatedAt = updatedAt),
            resourceSupply("2", type = type, updatedAt = updatedAt),
            resourceSupply("3", type = type, updatedAt = updatedAt),
            resourceSupply("4", type = type, updatedAt = updatedAt),
            resourceSupply("5", type = type, updatedAt = updatedAt),
            resourceSupply("6", type = type, updatedAt = updatedAt),
        )
    }


}