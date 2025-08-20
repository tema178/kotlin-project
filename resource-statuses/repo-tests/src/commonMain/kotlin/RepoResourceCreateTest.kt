
import models.*
import repo.DbRequest
import repo.DbResponseOk
import kotlin.test.*


abstract class RepoResourceCreateTest {
    abstract val repo: IRepoInitializable
    protected open val uuidNew = ResourceId("10000000-0000-0000-0000-000000000001")

    private val createObj = Resource(
        type = ResourceType("create object"),
        status = ResourceStatus("create"),
        updatedBy = UserId("owner-123")
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.create(DbRequest(createObj))
        val expected = createObj
        assertIs<DbResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.type, result.data.type)
        assertEquals(expected.status, result.data.status)
        assertNotEquals(ResourceId.NONE, result.data.id)
    }

    companion object : BaseInitResources("create") {
        override val initObjects: List<Resource> = emptyList()
    }
}
