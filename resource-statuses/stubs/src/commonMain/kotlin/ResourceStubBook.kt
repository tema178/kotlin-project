import kotlinx.datetime.Instant
import models.*

object ResourceStubBook{

    val RESOURCE_BOOK_AVAILABLE: Resource
    get() = Resource(
        id = ResourceId("PeaceAndWar"),
        name = "PeaceAndWar",
        type = ResourceType("book"),
        status = ResourceStatus("available"),
        updatedBy = UserId("admin"),
        updatedAt = Instant.fromEpochMilliseconds(1747690263),
        lock = Lock("lockedByAdmin")
    )

    val RESOURCE_BOOK_BUSY = RESOURCE_BOOK_AVAILABLE.copy(status = ResourceStatus("busy"))

}