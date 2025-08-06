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
        },
        invokeDeleteAd = {
            if (it.id == initAd.id)
                DbResponseOk(
                    data = initAd
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
        assertEquals(initAd.id, ctx.resource.id)
        assertEquals(initAd.type, ctx.resource.type)
        assertEquals(initAd.status, ctx.resource.status)
    }

    @Test
    fun repoDeleteNotFoundTest() = repoNotFoundTest(command)
}
