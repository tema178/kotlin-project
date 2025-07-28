package validation

import Context
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = ResourceId("123-234-abc-ABC"),
            type = ResourceType("abc"),
            status = ResourceStatus("abc")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
}

fun validationIdTrim(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = ResourceId(" \n\t 123-234-abc-ABC \n\t "),
            type = ResourceType("abc"),
            status = ResourceStatus("abc")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
}

fun validationIdEmpty(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = ResourceId(""),
            type = ResourceType("abc"),
            status = ResourceStatus("abc")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

fun validationIdFormat(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = ResourceId("!@#\$%^&*(),.{}"),
            type = ResourceType("abc"),
            status = ResourceStatus("abc")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
