package repo

import Context
import CorSettings
import RepositoryMock
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BizRepoReadTest {

    private val userId = UserId("321")
    private val command = Command.READ
    private val initAd = Resource(
        id = ResourceId("123"),
        status = ResourceStatus("abc"),
        type = ResourceType("abc"),
        updatedBy = userId
    )
    private val repo = RepositoryMock(
        invokeReadAd = {
            DbResponseOk(
                data = initAd,
            )
        }
    )
    private val settings = CorSettings(repoTest = repo)
    private val processor = StatusProcessor(settings)

    @Test
    fun repoReadSuccessTest() = runTest {
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            request = Resource(
                id = ResourceId("123"),
            ),
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertEquals(initAd.id, ctx.resource.id)
        assertEquals(initAd.type, ctx.resource.type)
        assertEquals(initAd.status, ctx.resource.status)
    }

    @Test
    fun repoReadNotFoundTest() = repoNotFoundTest(command)
}
