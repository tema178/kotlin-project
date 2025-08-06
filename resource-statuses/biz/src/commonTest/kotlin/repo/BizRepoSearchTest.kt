package repo

import Context
import CorSettings
import RepositoryMock
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoSearchTest {

    private val userId = UserId("321")
    private val command = Command.SEARCH
    private val initAd = Resource(
        id = ResourceId("123"),
        status = ResourceStatus("abc"),
        type = ResourceType("abc"),
        updatedBy = userId
    )
    private val repo = RepositoryMock(
        invokeSearchAd = {
            DbResponsesOk(
                data = listOf(initAd),
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = StatusProcessor(settings)

    @Test
    fun repoSearchSuccessTest() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            filterRequest = Filter(
                type = ResourceType("abc")
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(1, ctx.resources.size)
    }
}
