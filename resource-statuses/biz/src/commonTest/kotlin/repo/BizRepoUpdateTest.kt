package repo

import Context
import CorSettings
import RepositoryMock
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoUpdateTest {

    private val userId = UserId("321")
    private val command = Command.UPDATE
    private val initRes = Resource(
        id = ResourceId("123"),
        status = ResourceStatus("abc"),
        type = ResourceType("abc"),
        updatedBy = userId
    )
    private val repo = RepositoryMock(
        invokeReadRes = {
            DbResponseOk(
                data = initRes,
            )
        },
        invokeUpdateRes = {
            DbResponseOk(
                data = Resource(
                    id = ResourceId("123"),
                    status = ResourceStatus("xyz"),
                    type = ResourceType("xyz")
                )
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = StatusProcessor(settings)

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val adToUpdate = Resource(
            id = ResourceId("123"),
            status = ResourceStatus("xyz"),
            type = ResourceType("xyz")
        )
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            request = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(adToUpdate.id, ctx.resource.id)
        assertEquals(adToUpdate.type, ctx.resource.type)
        assertEquals(adToUpdate.status, ctx.resource.status)
    }

    @Test
    fun repoUpdateNotFoundTest() = repoNotFoundTest(command)
}
