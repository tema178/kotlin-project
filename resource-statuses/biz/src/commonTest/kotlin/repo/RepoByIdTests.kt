package repo

import Context
import CorSettings
import RepositoryMock
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private val initAd = Resource(
    id = ResourceId("123"),
    status = ResourceStatus("abc"),
    type = ResourceType("abc")
)
private val repo = RepositoryMock(
        invokeReadAd = {
            if (it.id == initAd.id) {
                DbResponseOk(
                    data = initAd,
                )
            } else errorNotFound(it.id)
        }
    )
private val settings = CorSettings(repoTest = repo)
private val processor = StatusProcessor(settings)

fun repoNotFoundTest(command: Command) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = ResourceId("12345"),
            status = ResourceStatus("xyz"),
            type = ResourceType("xyz")
        ),
    )
    processor.exec(ctx)
    assertEquals(State.FAILING, ctx.state)
    assertEquals(Resource(), ctx.resource)
    assertEquals(1, ctx.errors.size)
    assertNotNull(ctx.errors.find { it.code == "repo-not-found" }, "Errors must contain not-found")
}
