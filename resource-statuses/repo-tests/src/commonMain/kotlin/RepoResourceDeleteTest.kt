import models.*
import repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoResourceDeleteTest {
    abstract val repo: IRepo
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = ResourceId("ad-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.delete(DbIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbResponseOk>(result)
        assertEquals(deleteSucc.type, result.data.type)
        assertEquals(deleteSucc.status, result.data.status)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.read(DbIdRequest(notFoundId, lock = lockOld))

        assertIs<DbResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.delete(DbIdRequest(deleteConc.id, lock = lockBad))

        println(result)
        assertIs<DbResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitResources("delete") {
        override val initObjects: List<Resource> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
