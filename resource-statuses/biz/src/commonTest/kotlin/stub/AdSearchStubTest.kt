package stub

import Context
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class AdSearchStubTest {

    private val processor = StatusProcessor()
    val filter = Filter(type = ResourceType("book"))

    @Test
    fun read() = runTest {

        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            filterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.resources.size > 1)
        val first = ctx.resources.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.type.equals(filter.type))
        }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            filterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            filterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_TYPE,
            filterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
