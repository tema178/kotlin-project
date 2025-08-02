package stub

import Context
import ResourceStub
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class AdDeleteStubTest {

    private val processor = StatusProcessor()
    val id = ResourceId("666")

    @Test
    fun delete() = runTest {

        val ctx = Context(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            request = Resource(
                id = id,
            ),
        )
        processor.exec(ctx)

        val stub = ResourceStub.get()
        assertEquals(stub.id, ctx.resource.id)
        assertEquals(stub.type, ctx.resource.type)
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            request = Resource(),
        )
        processor.exec(ctx)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.DELETE,
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
            command = Command.DELETE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_TYPE,
            request = Resource(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
