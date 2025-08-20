import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import models.*

abstract class BaseInitResources(private val op: String): IInitObjects<Resource> {
    open val lockOld: Lock = Lock("20000000-0000-0000-0000-000000000001")
    open val lockBad: Lock = Lock("20000000-0000-0000-0000-000000000009")
    protected val updatedAtStub: Instant = Clock.System.now()

    fun createInitTestModel(
        suf: String,
        ownerId: UserId = UserId("owner-123"),
        lock: Lock = lockOld,
    ) = Resource(
        id = ResourceId("ad-repo-$op-$suf"),
        type = ResourceType("$suf stub type"),
        status = ResourceStatus("$suf stub status"),
        updatedBy = ownerId,
        updatedAt = updatedAtStub,
        lock = lock,
    )
}
