import models.*
import repo.DbResponseErr
import repo.DbIdRequest
import repo.DbResponseOk
import repo.IRepo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoResourceReadTest {
    abstract val repo: IRepo
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.read(DbIdRequest(readSucc.id))

        assertIs<DbResponseOk>(result)
        assertEquals(readSucc, result.data.copy(updatedAt = updatedAtStub))
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.read(DbIdRequest(notFoundId))

        assertIs<DbResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitResources("delete") {
        override val initObjects: List<Resource> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = ResourceId("ad-repo-read-notFound")

    }
}
