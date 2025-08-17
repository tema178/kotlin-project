import kotlinx.coroutines.test.runTest
import models.Resource
import models.ResourceType
import repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RepositoryMockTest {
    private val repo = RepositoryMock(
        invokeCreateRes = { DbResponseOk(ResourceStub.prepareResult { type = ResourceType("create") }) },
        invokeReadRes = { DbResponseOk(ResourceStub.prepareResult { type = ResourceType("read") }) },
        invokeUpdateRes = { DbResponseOk(ResourceStub.prepareResult { type = ResourceType("update") }) },
        invokeDeleteRes = { DbResponseOk(ResourceStub.prepareResult { type = ResourceType("delete") }) },
        invokeSearchRes = { DbResponsesOk(listOf(ResourceStub.prepareResult { type = ResourceType("search") })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.create(DbRequest(Resource()))
        assertIs<DbResponseOk>(result)
        assertEquals(ResourceType("create"), result.data.type)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.read(DbIdRequest(Resource()))
        assertIs<DbResponseOk>(result)
        assertEquals(ResourceType("read"), result.data.type)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.update(DbRequest(Resource()))
        assertIs<DbResponseOk>(result)
        assertEquals(ResourceType("update"), result.data.type)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.delete(DbIdRequest(Resource()))
        assertIs<DbResponseOk>(result)
        assertEquals(ResourceType("delete"), result.data.type)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.search(DbFilterRequest())
        assertIs<DbResponsesOk>(result)
        assertEquals(ResourceType("search"), result.data.first().type)
    }

}
