package validation

import Context
import StatusProcessor
import kotlinx.coroutines.test.runTest
import models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = ResourceStub.get()

fun validationStatusCorrect(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = stub.id,
            status = ResourceStatus("abc"),
            type = ResourceType("abc"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals(ResourceStatus("abc"), ctx.validated.status)
}

fun validationStatusTrim(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = stub.id,
            status = ResourceStatus(" \n\t abc \t\n "),
            type = ResourceType("abc"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals(ResourceStatus("abc"), ctx.validated.status)
}

fun validationStatusEmpty(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = stub.id,
            status = ResourceStatus(""),
            type = ResourceType("abc"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("status", error?.field)
    assertContains(error?.message ?: "", "status")
}

fun validationStatusSymbols(command: Command, processor: StatusProcessor) = runTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        request = Resource(
            id = ResourceId("123"),
            status = ResourceStatus("!@#$%^&*(),.{}"),
            type = ResourceType("abc"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("status", error?.field)
    assertContains(error?.message ?: "", "status")
}
