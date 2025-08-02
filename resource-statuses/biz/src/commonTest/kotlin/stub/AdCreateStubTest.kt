package stub

import Context
import ResourceStub
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class AdCreateStubTest {

    private val processor = StatusProcessor()
    val id = ResourceId("666")
    val type = ResourceType("book")

    @Test
    fun create() = runTest {

        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            request = Resource(
                id = id,
                type = type
            ),
        )
        processor.exec(ctx)
        assertEquals(ResourceStub.get().id, ctx.resource.id)
        assertEquals(type, ctx.resource.type)
    }

    @Test
    fun badType() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_TYPE,
            request = Resource(
                id = id,
                type = ResourceType.DEFAULT,
            ),
        )
        processor.exec(ctx)
        assertEquals("type", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            request = Resource(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            request = Resource(
                id = id,
                type = type
            ),
        )
        processor.exec(ctx)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
