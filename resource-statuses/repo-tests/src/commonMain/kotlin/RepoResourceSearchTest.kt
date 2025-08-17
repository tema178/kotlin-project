import models.*
import repo.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoResourceSearchTest {
    abstract val repo: IRepo

    protected open val initializedObjects: List<Resource> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.search(DbFilterRequest(ownerId = searchOwnerId))
        assertIs<DbResponsesOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[2]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.map { res -> res.copy(updatedAt = updatedAtStub)}.sortedBy { it.id.asString() })
    }


    companion object: BaseInitResources("search") {

        val searchOwnerId = UserId("owner-124")
        override val initObjects: List<Resource> = listOf(
            createInitTestModel("ad1"),
            createInitTestModel("ad2", ownerId = searchOwnerId),
            createInitTestModel("ad4", ownerId = searchOwnerId),
        )
    }
}
