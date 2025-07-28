package validation

import Context
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = ResourceStub.get()

fun validationTypeCorrect(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = stub.id,
            type = ResourceType("abc"),
            status = ResourceStatus("abc"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals(ResourceType("abc"), ctx.validated.type)
}

fun validationTypeTrim(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = stub.id,
            type = ResourceType(" \n\t abc \t\n "),
            status = ResourceStatus("abc"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals(ResourceType("abc"), ctx.validated.type)
}

fun validationTypeEmpty(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = stub.id,
            type = ResourceType(""),
            status = ResourceStatus("abc"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("type", error?.field)
    assertContains(error?.message ?: "", "type")
}

fun validationTypeSymbols(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = ResourceId("123"),
            type = ResourceType("!@#$%^&*(),.{}"),
            status = ResourceStatus("abc"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("type", error?.field)
    assertContains(error?.message ?: "", "type")
}
