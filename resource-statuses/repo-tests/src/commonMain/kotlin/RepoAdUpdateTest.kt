import models.*
import repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoAdUpdateTest {
    abstract val repo: IRepo
    protected open val updateSucc = initObjects[0]
    protected val updateIdNotFound = ResourceId("ad-repo-update-not-found")

    private val reqUpdateSucc by lazy {
        Resource(
            id = updateSucc.id,
            type = ResourceType("update object"),
            status = ResourceStatus("update object description"),
            updatedBy = UserId("owner-123")
        )
    }
    private val reqUpdateNotFound = Resource(
        id = updateIdNotFound,
        type = ResourceType("update object not found"),
        status = ResourceStatus("update object not found description"),
        updatedBy = UserId("owner-123")
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.update(DbRequest(reqUpdateSucc))
        assertIs<DbResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.status, result.data.status)
        assertEquals(reqUpdateSucc.type, result.data.type)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.update(DbRequest(reqUpdateNotFound))
        assertIs<DbResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitResources("update") {
        override val initObjects: List<Resource> = listOf(
            createInitTestModel("update"),
        )
    }
}
