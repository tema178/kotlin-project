import models.*

data class Entity(
    val id: String? = null,
    val name: String? = null,
    val type: String? = null,
    val status: String? = null,
    val ownerId: String? = null,
    val lock: String? = null
) {
    constructor(model: Resource): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        name = model.name,
        type = model.type.asString().takeIf { it.isNotBlank() },
        status = model.status.asString().takeIf { it.isNotBlank() },
        ownerId = model.updatedBy.asString().takeIf { it.isNotBlank() },
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = Resource(
        id = id?.let { ResourceId(it) }?: ResourceId.NONE,
        name = name ?: "",
        type = type?.let { ResourceType(it) }?: ResourceType.DEFAULT,
        status = status?.let { ResourceStatus(it) }?: ResourceStatus.NONE,
        updatedBy = ownerId?.let { UserId(it) }?: UserId.NONE,
        lock = lock?.let { Lock(it) } ?: Lock.NONE,
    )
}
