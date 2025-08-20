package repo

import Context
import CorSettings
import RepositoryMock
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BizRepoDeleteTest {

    private val userId = UserId("321")
    private val command = Command.DELETE
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
        invokeDeleteRes = {
            if (it.id == initRes.id)
                DbResponseOk(
                    data = initRes
                )
            else DbResponseErr()
        }
    )
    private val settings by lazy {
        CorSettings(
            repoTest = repo
        )
    }
    private val processor = StatusProcessor(settings)

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val adToUpdate = Resource(
            id = ResourceId("123")
        )
        val ctx = Context(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            request = adToUpdate,
        )
        processor.exec(ctx)
        assertEquals(State.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initRes.id, ctx.resource.id)
        assertEquals(initRes.type, ctx.resource.type)
        assertEquals(initRes.status, ctx.resource.status)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
