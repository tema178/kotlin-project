package repo

import Context
import CorSettings
import RepositoryMock
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizRepoCreateTest {

    private val userId = UserId("321")
    private val command = Command.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"
    private val repo = RepositoryMock(
        invokeCreateAd = {
            DbResponseOk(
                data = Resource(
                    id = ResourceId(uuid),
                    status = it.resource.status,
                    type = it.resource.type,
                    updatedBy = userId
                )
            )
        }
    )
    private val settings = CorSettings(
        repoTest = repo
    )
    private val processor = StatusProcessor(settings)

    @Test
    fun repoCreateSuccessTest() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            request = Resource(
                status = ResourceStatus("abc"),
                type = ResourceType("abc"),
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertNotEquals(ResourceId.NONE, ctx.resource.id)
        assertEquals(ResourceType("abc"), ctx.resource.type)
        assertEquals(ResourceStatus("abc"), ctx.resource.status)
    }
}
