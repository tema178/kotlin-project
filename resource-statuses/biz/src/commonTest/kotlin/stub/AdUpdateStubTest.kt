package stub

import Context
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class AdUpdateStubTest {

    private val processor = StatusProcessor()
    val id = ResourceId("666")
    val type = ResourceType("book")
    val status = ResourceStatus("available")

    @Test
    fun update() = runTest {

        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            request = Resource(
                id = id,
                type = type,
                status = status
            ),
        )
        processor.exec(ctx)
        assertEquals(id, ctx.resource.id)
        assertEquals(type, ctx.resource.type)
        assertEquals(status, ctx.resource.status)
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            resource = Resource(),
        )
        processor.exec(ctx)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badType() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_TYPE,
            resource = Resource(
                id = id,
                type = ResourceType(""),
                status = status
            ),
        )
        processor.exec(ctx)
        assertEquals("type", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            resource = Resource(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.UPDATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_SEARCH_STRING,
            resource = Resource(
                id = id,
                type = type,
                status = status
            ),
        )
        processor.exec(ctx)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
