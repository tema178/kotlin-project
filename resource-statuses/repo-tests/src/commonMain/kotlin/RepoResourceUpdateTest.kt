import models.*
import repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoResourceUpdateTest {
    abstract val repo: IRepo
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = ResourceId("ad-repo-update-not-found")
    protected val lockBad = Lock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = Lock("20000000-0000-0000-0000-000000000002")


    private val reqUpdateSucc by lazy {
        Resource(
            id = updateSucc.id,
            type = ResourceType("update object"),
            status = ResourceStatus("update object description"),
            updatedBy = UserId("owner-123"),
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = Resource(
        id = updateIdNotFound,
        type = ResourceType("update object not found"),
        status = ResourceStatus("update object not found description"),
        updatedBy = UserId("owner-123"),
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc by lazy {
        Resource(
            id = updateConc.id,
            type = ResourceType("update object"),
            status = ResourceStatus("update object description"),
            updatedBy = UserId("owner-123"),
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.update(DbRequest(reqUpdateSucc))
        assertIs<DbResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.status, result.data.status)
        assertEquals(reqUpdateSucc.type, result.data.type)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.update(DbRequest(reqUpdateNotFound))
        assertIs<DbResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.update(DbRequest(reqUpdateConc))
        assertIs<DbResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data.copy(updatedAt = updatedAtStub))
    }

    companion object : BaseInitResources("update") {
        override val initObjects: List<Resource> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
